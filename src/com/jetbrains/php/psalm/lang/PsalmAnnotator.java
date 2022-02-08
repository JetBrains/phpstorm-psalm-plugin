package com.jetbrains.php.psalm.lang;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.highlighter.PhpHighlightingData;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.psalm.types.PsalmAliasTypeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsalmAnnotator implements Annotator {
  @Override
  public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
    if (PhpPsiUtil.isOfType(element, PhpDocTokenTypes.DOC_IDENTIFIER)) {
      PsiElement parent = element.getParent();
      if (parent instanceof PhpDocTag && PsalmAliasTypeProvider.PSALM_TYPE_TAG_NAME.equals(((PhpDocTag)parent).getName())) {
        highlightDocIdentifier(element, holder);
      }
    }
    else if (element instanceof PhpDocTag && PsalmAliasTypeProvider.PSALM_IMPORT_TYPE_TAG_NAME.equals(((PhpDocTag)element).getName())) {
      PsiElement identifier = PhpPsiUtil.getChildOfType(element, PhpDocTokenTypes.DOC_IDENTIFIER);
      if (identifier == null) return;
      highlightDocIdentifier(identifier, holder);

      PsiElement docType = PhpPsiUtil.getNextSiblingByCondition(identifier, PhpDocType.class::isInstance);
      if (docType != null) {
        highlightAliasAfterDocType(holder, docType);
      }
    }
  }

  private static void highlightAliasAfterDocType(@NotNull AnnotationHolder holder, PsiElement identifier) {
    identifier = PhpPsiUtil.getNextSiblingIgnoreWhitespace(identifier, true);
    if (identifier == null || !identifier.getText().equals("as")) return;
    identifier = PhpPsiUtil.getNextSiblingIgnoreWhitespace(identifier, true);
    if (PhpPsiUtil.isOfType(identifier, PhpDocTokenTypes.DOC_IDENTIFIER)) {
      highlightDocIdentifier(identifier, holder);
    }
  }

  @Nullable
  private static PsiElement getNextIdentifierAfterElementWithText(PsiElement element, String text) {
    element = PhpPsiUtil.getNextSiblingIgnoreWhitespace(element, true);
    if (element != null && element.getText().equals(text)) {
      element = PhpPsiUtil.getNextSiblingIgnoreWhitespace(element, true);
    }
    return PhpPsiUtil.isOfType(element, PhpDocTokenTypes.DOC_IDENTIFIER) ? element : null;
  }

  private static void highlightDocIdentifier(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
    holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
      .range(element)
      .textAttributes(PhpHighlightingData.DOC_IDENTIFIER)
      .create();
  }
}
