package com.jetbrains.php.psalm.types;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.SyntaxTraverser;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.JBIterable;
import com.jetbrains.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes.*;
import static com.jetbrains.php.lang.psi.PhpPsiUtil.isOfType;

public class PsalmParamTypeProvider implements PhpTypeProvider4 {

  @Override
  public char getKey() {
    return 'ᢒ';
  }

  @Override
  public @Nullable PhpType getType(PsiElement element) {
    return element instanceof PhpDocType ? parsePsalmType((PhpDocType)element) : null;
  }

  private static final Collection<String> GENERIC_ARRAYS_NAMES = Set.of("array", "list");

  private static @Nullable PhpType parsePsalmType(@NotNull PhpDocType docType) {
    String name = StringUtil.notNullize(docType.getName());
    PsiElement attributes = PhpPsiUtil.getChildOfType(docType, PhpDocElementTypes.phpDocAttributeList);
    if (attributes == null || !GENERIC_ARRAYS_NAMES.contains(StringUtil.toLowerCase(name))) {
      return null;
    }
    return valueDocTypes(docType)
      .map(PhpTypedElement::getType)
      .reduce(new PhpType(), PhpType::add).pluralise();
  }

  @NotNull
  private static JBIterable<PhpDocType> valueDocTypes(@NotNull PhpDocType docType) {
    PsiElement attributes = PhpPsiUtil.getChildOfType(docType, PhpDocElementTypes.phpDocAttributeList);
    if (attributes == null) {
      return JBIterable.empty();
    }
    PsiElement attributesFirstChild = attributes.getFirstChild();
    if (!(isOfType(attributesFirstChild, DOC_LAB) && isOfType(attributes.getLastChild(), DOC_RAB))) {
      return JBIterable.empty();
    }
    PsiElement elementToFindCommentsAfter = ObjectUtils.notNull(PhpPsiUtil.getChildOfType(attributes, DOC_COMMA), attributesFirstChild);
    return SyntaxTraverser.psiApi()
      .siblings(elementToFindCommentsAfter)
      .filter(PhpDocType.class);
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
