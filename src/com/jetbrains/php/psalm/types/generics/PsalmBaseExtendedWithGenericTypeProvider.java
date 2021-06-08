package com.jetbrains.php.psalm.types.generics;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.PhpLangUtil;
import com.jetbrains.php.lang.documentation.phpdoc.PhpDocUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.impl.tags.PhpDocTagImpl;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpClassMember;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.jetbrains.php.psalm.types.PsalmExtendedTypeProvider.TEMPLATES_NAMES;

public abstract class PsalmBaseExtendedWithGenericTypeProvider implements PhpTypeProvider4 {
  @Override
  public @Nullable PhpType getType(PsiElement element) {
    return null;
  }

  @Override
  public @Nullable PhpType complete(String expression, Project project) {
    PhpIndex index = PhpIndex.getInstance(project);
    int dot = expression.lastIndexOf('.');
    if (dot < 0) return null;
    String classRef = expression.substring(2, dot);
    Map<String, String> extendedClassesToSubstitutedTemplates = StreamEx.of(index.getBySignature(classRef))
      .select(PhpClass.class)
      .map(PsalmBaseExtendedWithGenericTypeProvider::getExtendedClassAndSubstitutedTemplate).nonNull()
      .mapToEntry(c -> c.getFirst(), c -> c.getSecond())
      .toMap((s, s1) -> s);
    if (extendedClassesToSubstitutedTemplates.isEmpty()) return null;
    return StreamEx.of(index.getBySignature(expression))
      .select(PhpClassMember.class)
      .map(PsalmBaseExtendedWithGenericTypeProvider::getSubstitutedTemplateInfo)
      .map(info -> substituteTemplateType(extendedClassesToSubstitutedTemplates, info)).nonNull()
      .reduce(new PhpType(), PhpType::add, PhpType::or);
  }

  @Nullable
  private static String substituteTemplateType(Map<String, String> extendedTemplates, @Nullable TemplateInfo info) {
    return info != null && extendedTemplates.containsKey(info.myContainingClassFQN) ? PhpType.pluralise(
      extendedTemplates.get(info.myContainingClassFQN), info.myDimension) : null;
  }

  private static @Nullable TemplateInfo getSubstitutedTemplateInfo(PhpClassMember m) {
    PhpClass containingClass = m.getContainingClass();
    if (containingClass == null) return null;
    Collection<String> templates = getTemplates(containingClass.getDocComment());
    for (String type : m.getDocType().getTypes()) {
      String normalizedType = normalize(type);
      if (templates.contains(normalizedType)) {
        return new TemplateInfo(containingClass.getFQN(), PhpType.getPluralDimension(type));
      }
    }

    return null;
  }

  private static class TemplateInfo {
    private final String myContainingClassFQN;
    private final int myDimension;

    private TemplateInfo(String fqn, int dimension) {
      myContainingClassFQN = fqn;
      myDimension = dimension;
    }
  }

  private static Collection<String> getTemplates(@Nullable PhpDocComment comment) {
    if (comment == null) return Collections.emptyList();
    Collection<String> res = new HashSet<>();
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

  private static @Nullable Pair<String, String> getExtendedClassAndSubstitutedTemplate(PhpClass e) {
    PhpDocComment docComment = e.getDocComment();
    if (docComment == null) return null;
    return Arrays.stream(PsalmTemplatesCustomDocTagValueStubProvider.EXTENDED_NAMES)
      .flatMap(name -> Arrays.stream(docComment.getTagElementsByName(name)))
      .map(t -> ((PhpDocTagImpl)t).getCustomTagValue()).filter(Objects::nonNull)
      .map(PsalmTemplatesCustomDocTagValueStubProvider::decodeExtendedClassAndTemplate)
      .findFirst().orElse(null);
  }

  @Override
  public Collection<? extends PhpNamedElement> getBySignature(String expression,
                                                              Set<String> visited,
                                                              int depth,
                                                              Project project) {
    return null;
  }
}
