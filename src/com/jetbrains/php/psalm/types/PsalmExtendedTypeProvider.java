package com.jetbrains.php.psalm.types;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
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
      String name = ((PhpDocType)element).getName();
      if (getTypeNames(docComment, "@template").contains(name) ||
          getTypeNames(docComment, "@psalm-type").contains(name) ||
          getImportedTypeNames(docComment).contains(name)) {
        return PhpType.MIXED;
      }
    }
    return null;
  }

  private static Collection<String> getTypeNames(@Nullable PhpDocComment docComment, String tagName) {
    return tagValues(docComment, tagName)
      .map(value -> PhpPsiUtil.getChildOfType(value, DOC_IDENTIFIER)).filter(Objects::nonNull)
      .map(PsiElement::getText)
      .collect(Collectors.toSet());
  }

  @NotNull
  private static Stream<PhpPsiElement> tagValues(@Nullable PhpDocComment docComment, String tagName) {
    Stream<PhpDocTag> tagElements = docComment != null ? Arrays.stream(docComment.getTagElementsByName(tagName)) : Stream.empty();
    return tagElements.map(PhpPsiElement::getFirstPsiChild).filter(Objects::nonNull);
  }

  private static Collection<String> getImportedTypeNames(@Nullable PhpDocComment docComment) {
    return tagValues(docComment, "@psalm-import-type")
      .map(PsalmExtendedTypeProvider::getImportedName).filter(Objects::nonNull)
      .map(PsiElement::getText)
      .collect(Collectors.toSet());
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
