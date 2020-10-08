package com.jetbrains.php.psalm.types;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.lang.documentation.phpdoc.PhpDocUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
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
  @Override
  public char getKey() {
    return '\u1890';
  }

  @Override
  public @Nullable PhpType getType(PsiElement element) {
    if (element instanceof PhpDocType) {
      PhpDocComment docComment = PhpPsiUtil.getParentByCondition(element, PhpDocComment.INSTANCEOF);
      if (declaredInCustomTypeDocTag(docComment, ((PhpDocType)element).getName())) {
        return PhpType.MIXED;
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

  public static @NotNull Collection<String> getTemplates(@Nullable PhpDocComment docComment) {
    return getTypeNames(docComment, "@template", "@psalm-template", "@template-covariant");
  }

  private static Collection<String> getTypeNames(@Nullable PhpDocComment docComment, String... tagNames) {
    return getNamesInCurrentCommentOrClass(docComment, c -> tagValues(c, tagNames)
      .map(value -> PhpPsiUtil.getChildOfType(value, DOC_IDENTIFIER)).filter(Objects::nonNull)
      .map(PsiElement::getText)
      .collect(Collectors.toSet()));
  }

  private static Collection<String> getNamesInCurrentCommentOrClass(PhpDocComment docComment, Function<@Nullable PhpDocComment, Collection<String>> f) {
    if (docComment == null) {
      return Collections.emptySet();
    }
    Collection<String> names = f.apply(docComment);
    PsiElement owner = docComment.getOwner();
      if (!(owner instanceof PhpClass)) {
        if (!(owner instanceof com.jetbrains.php.lang.psi.elements.Function)) {
          com.jetbrains.php.lang.psi.elements.Function function =
            PhpPsiUtil.getParentByCondition(owner, com.jetbrains.php.lang.psi.elements.Function.INSTANCEOF);
          if (function != null) {
            names = f.apply(function.getDocComment());
          }
        }
        if (names.isEmpty()) {
          PhpClass containingClass = PhpPsiUtil.getParentByCondition(owner, PhpClass.INSTANCEOF);
          if (containingClass != null) {
            return f.apply(containingClass.getDocComment());
          }
        }
      }
    return names;
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
      .collect(Collectors.toSet()));
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
    return null;
  }

  @Override
  public Collection<? extends PhpNamedElement> getBySignature(String expression, Set<String> visited, int depth, Project project) {
    return null;
  }
}
