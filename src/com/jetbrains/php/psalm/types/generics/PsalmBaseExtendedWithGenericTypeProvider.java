package com.jetbrains.php.psalm.types.generics;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.PhpLangUtil;
import com.jetbrains.php.lang.documentation.phpdoc.PhpDocUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.impl.tags.PhpDocTagImpl;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.*;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

import static com.jetbrains.php.psalm.types.PsalmExtendedTypeProvider.TEMPLATES_NAMES;

public abstract class PsalmBaseExtendedWithGenericTypeProvider implements PhpTypeProvider4 {
  @Override
  public @Nullable PhpType getType(PsiElement element) {
    if (element instanceof MemberReference) {
      for (PhpNamedElement member : ((MemberReference)element).resolveLocal()) {
        if (member instanceof PhpClassMember && getSubstitutedTemplateInfo(((PhpClassMember)member)) != null) {
          // In case of successful local resolve no signature will be added
          // so add these signatures again in case this TypeProvider possibly can infer generic templates
          return getSignatureType((MemberReference)element);
        }
      }
    }
    return null;
  }

  @NotNull
  private PhpType getSignatureType(MemberReference element) {
    PhpType type = new PhpType();
    for (String part : element.getSignatureParts()) {
      if (getSignatureKey().signed(part)) {
        type.add(part);
      }
    }
    return type;
  }

  protected abstract @NotNull PhpCharBasedTypeKey getSignatureKey();

  @Override
  public char getKey() {
    return getSignatureKey().getKey();
  }

  @Override
  public @Nullable PhpType complete(String expression, Project project) {
    PhpIndex index = PhpIndex.getInstance(project);
    String classRef = getClassRef(expression);
    if (classRef == null) return null;
    Map<String, List<String>> extendedClassesToSubstitutedTemplates = getExtendedClassesToSubstitutedTemplates(index, classRef);
    if (extendedClassesToSubstitutedTemplates.isEmpty()) return null;
    return resolveTargetMembers(expression, index, extendedClassesToSubstitutedTemplates)
      .map(PsalmBaseExtendedWithGenericTypeProvider::getSubstitutedTemplateInfo)
      .map(info -> substituteTemplateType(extendedClassesToSubstitutedTemplates, info)).nonNull()
      .reduce(new PhpType(), PhpType::add, PhpType::or);
  }

  protected StreamEx<? extends PhpClassMember> resolveTargetMembers(String expression,
                                                          PhpIndex index,
                                                          Map<String, List<String>> extendedClassesToSubstitutedTemplates) {
    return StreamEx.of(index.getBySignature(expression)).select(PhpClassMember.class);
  }

  @Nullable
  protected String getClassRef(String expression) {
    int dot = expression.lastIndexOf('.');
    if (dot < 0) return null;
    return expression.substring(2, dot);
  }

  public static Map<String, List<String>> getExtendedClassesToSubstitutedTemplates(PhpIndex index, String classRef) {
    return decodeExtendedClassesAndSubstitutedTemplates(index, classRef).nonNull()
      .mapToEntry(c -> c.getFirst(), c -> c.getSecond())
      .toMap((s, s1) -> s);
  }

  @NotNull
  private static StreamEx<Pair<String, List<String>>> decodeExtendedClassesAndSubstitutedTemplates(PhpIndex index, String classRef) {
    if (PsalmGenericTypeProvider.KEY.signed(classRef)) {
      String encodedSubstitutionInfo = ContainerUtil.getLastItem(PhpParameterBasedTypeProvider.extractSignatures(classRef, 2));
      if (encodedSubstitutionInfo != null) {
        return StreamEx.of(PsalmTemplatesCustomDocTagValueStubProvider.decodeExtendedClassAndTemplate(encodedSubstitutionInfo))
          .flatMap(decoded -> StreamEx.of(decoded).append(expandExtendedTemplates(index, decoded)));
      }
      return StreamEx.empty();
    } else {
      return StreamEx.of(index.getBySignature(classRef))
        .select(PhpClass.class)
        .flatMap(PsalmBaseExtendedWithGenericTypeProvider::extendedClassesAndSubstitutedTemplates);
    }
  }

  private static Stream<Pair<String, List<String>>> expandExtendedTemplates(PhpIndex index, Pair<String, List<String>> decoded) {
    return index.getAnyByFQN(decoded.first).stream()
      .flatMap(PsalmBaseExtendedWithGenericTypeProvider::extendedClassesWithFallthroughTemplates)
      .map(extended -> Pair.create(extended, decoded.second));
  }

  private static Stream<String> extendedClassesWithFallthroughTemplates(PhpClass c) {
    List<String> templates = ContainerUtil.map(getTemplates(c.getDocComment()), PhpLangUtil::toFQN);
    if (templates.isEmpty()) return Stream.empty();
    return extendedClassesAndSubstitutedTemplates(c)
      .filter(decoded -> decoded.second.equals(templates))
      .map(decoded -> decoded.getFirst());
  }

  @Nullable
  private static String substituteTemplateType(Map<String, List<String>> classesToExtendedTemplates, @Nullable TemplateInfo info) {
    if (info != null && classesToExtendedTemplates.containsKey(info.myContainingClassFQN)) {
      List<String> extendedTemplates = classesToExtendedTemplates.get(info.myContainingClassFQN);
      int templateIndex = info.myTemplateIndex;
      // hardcode IteratorAggregate usage with single templates since both variants are present in the userland
      if (extendedTemplates.size() == 1 && info.myContainingClassFQN.equals("\\IteratorAggregate")) {
        templateIndex = 0;
      }
      if (templateIndex < extendedTemplates.size() && !extendedTemplates.isEmpty()) {
        return PhpType.pluralise(extendedTemplates.get(templateIndex), info.myDimension);
      }
    }
    return null;
  }

  private static @Nullable TemplateInfo getSubstitutedTemplateInfo(PhpClassMember m) {
    PhpClass containingClass = m.getContainingClass();
    if (containingClass == null) return null;
    List<String> templates = getTemplates(containingClass.getDocComment());
    for (String type : m.getDocType().getTypes()) {
      String normalizedType = normalize(type);
      int templateIndex = templates.indexOf(normalizedType);
      if (templateIndex >= 0) {
        return new TemplateInfo(containingClass.getFQN(), templateIndex, PhpType.getPluralDimension(type));
      }
    }

    return null;
  }

  private static class TemplateInfo {
    private final String myContainingClassFQN;
    private final int myTemplateIndex;
    private final int myDimension;

    private TemplateInfo(String fqn, int templateIndex, int dimension) {
      myContainingClassFQN = fqn;
      myTemplateIndex = templateIndex;
      myDimension = dimension;
    }
  }

  private static List<String> getTemplates(@Nullable PhpDocComment comment) {
    if (comment == null) return Collections.emptyList();
    List<String> res = new ArrayList<>();
    PhpDocUtil.processTagElementsByNames(comment, tag -> {
      ContainerUtil.addIfNotNull(res, ((PhpDocTagImpl)tag).getCustomTagValue());
      return true;
    }, TEMPLATES_NAMES);
    return res;
  }

  private static String normalize(@NotNull String s) {
    while (s.startsWith("#")) {
      s = s.substring(2);
    }
    return PhpType.unpluralize(PhpLangUtil.toPresentableFQN(s), PhpType.getPluralDimension(s));
  }

  @NotNull
  private static Stream<Pair<String, List<String>>> extendedClassesAndSubstitutedTemplates(PhpClass e) {
    PhpDocComment docComment = e.getDocComment();
    if (docComment == null) return Stream.empty();
    return Arrays.stream(PsalmTemplatesCustomDocTagValueStubProvider.EXTENDED_NAMES)
      .flatMap(name -> Arrays.stream(docComment.getTagElementsByName(name)))
      .map(t -> ((PhpDocTagImpl)t).getCustomTagValue()).filter(Objects::nonNull)
      .map(PsalmTemplatesCustomDocTagValueStubProvider::decodeExtendedClassAndTemplate);
  }

  @Override
  public Collection<? extends PhpNamedElement> getBySignature(String expression,
                                                              Set<String> visited,
                                                              int depth,
                                                              Project project) {
    return null;
  }

  @Override
  public boolean interceptsNativeSignature() {
    return true;
  }
}
