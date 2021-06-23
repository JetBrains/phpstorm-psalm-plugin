package com.jetbrains.php.psalm.types;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.lang.documentation.phpdoc.PhpDocUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpCharBasedTypeKey;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes.DOC_IDENTIFIER;

public class PsalmExtendedTypeProvider implements PhpTypeProvider4 {
  private static final PhpCharBasedTypeKey KEY = new PhpCharBasedTypeKey() {
    @Override
    public char getKey() {
      return '\u1890';
    }
  };
  public static final String[] TEMPLATES_NAMES = {"@template", "@psalm-template", "@template-covariant"};


  @Override
  public char getKey() {
    return KEY.getKey();
  }

  @Override
  public @Nullable PhpType getType(PsiElement element) {
    if (element instanceof PhpDocType) {
      PhpDocComment docComment = PhpPsiUtil.getParentByCondition(element, PhpDocComment.INSTANCEOF);
      String name = ((PhpDocType)element).getName();
      if (declaredInCustomTypeDocTag(docComment, name)) {
        return new PhpType().add(KEY.sign(name));
      }
    }
    return null;
  }

  private static boolean declaredInCustomTypeDocTag(@Nullable PhpDocComment docComment, String name) {
    return getTemplates(docComment).contains(name) || getCustomTypes(docComment).contains(name);
  }

  public static Collection<String> getCustomTypes(PhpDocComment docComment) {
    return ContainerUtil.union(getTypeNames(docComment, "@psalm-type"), getImportedTypeNames(docComment));
  }

  public static @NotNull List<String> getTemplates(@Nullable PhpDocComment docComment) {
    return docComment != null ? CachedValuesManager.getCachedValue(docComment, () -> CachedValueProvider.Result.create(
      getTypeNames(docComment, TEMPLATES_NAMES), PsiModificationTracker.MODIFICATION_COUNT)) : Collections.emptyList();
  }

  private static List<String> getTypeNames(@Nullable PhpDocComment docComment, String... tagNames) {
    return getNamesInCurrentCommentOrClass(docComment, c -> tagValues(c, tagNames)
      .map(value -> PhpPsiUtil.getChildOfType(value, DOC_IDENTIFIER)).filter(Objects::nonNull)
      .map(PsiElement::getText)
      .collect(Collectors.toList()));
  }

  private static List<String> getNamesInCurrentCommentOrClass(PhpDocComment docComment, Function<@Nullable PhpDocComment, List<String>> f) {
    if (docComment == null) {
      return Collections.emptyList();
    }
    List<String> names = Collections.emptyList();
    PsiElement owner = docComment.getOwner();
    while (owner != null) {
      if (owner instanceof PhpPsiElement) {
        PhpDocComment comment =
          owner instanceof PhpNamedElement ? ((PhpNamedElement)owner).getDocComment() : PhpPsiUtil.getDocCommentFor((PhpPsiElement)owner);
        names = ContainerUtil.concat(f.apply(comment), names);
        if (owner instanceof PhpClass && !((PhpClass)owner).isAnonymous()) break;
      }
      owner = owner.getParent();
    }
    return names.stream().distinct().collect(Collectors.toList());
  }

  @NotNull
  private static Stream<PhpPsiElement> tagValues(@Nullable PhpDocComment docComment, String... tagNames) {
    if (docComment == null) {
      return Stream.empty();
    }
    Collection<PhpDocTag> docTags = new ArrayList<>();
    PhpDocUtil.processTagElementsByNames(docComment, tag -> {
      docTags.add(tag);
      return true;
    }, tagNames);
    return docTags.stream().map(PhpPsiElement::getFirstPsiChild).filter(Objects::nonNull);
  }

  private static Collection<String> getImportedTypeNames(@Nullable PhpDocComment docComment) {
    return getNamesInCurrentCommentOrClass(docComment, c -> tagValues(c, "@psalm-import-type")
      .map(PsalmExtendedTypeProvider::getImportedName).filter(Objects::nonNull)
      .map(PsiElement::getText)
      .distinct()
      .collect(Collectors.toList()));
  }

  private static PsiElement getImportedName(PhpPsiElement value) {
    List<PsiElement> docIdentifiers = getDocIdentifiers(value);
    // T1 from T2 as T3: 5 tokens by contract
    return docIdentifiers.size() >= 5 ? ContainerUtil.getLastItem(docIdentifiers) : ContainerUtil.getFirstItem(docIdentifiers);
  }

  @NotNull
  private static List<PsiElement> getDocIdentifiers(PhpPsiElement value) {
    List<PsiElement> docIdentifiers = new ArrayList<>();
    PsiElement child = value.getFirstChild();
    while (child != null) {
      if (PhpPsiUtil.isOfType(child, DOC_IDENTIFIER)) {
        docIdentifiers.add(child);
      }
      child = child.getNextSibling();
    }
    return docIdentifiers;
  }

  @Override
  public @Nullable PhpType complete(String expression, Project project) {
    return PhpType.isPluralType(expression) ? PhpType.ARRAY : PhpType.MIXED;
  }

  @Override
  public Collection<? extends PhpNamedElement> getBySignature(String expression, Set<String> visited, int depth, Project project) {
    return null;
  }
}
