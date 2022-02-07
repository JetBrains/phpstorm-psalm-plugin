package com.jetbrains.php.psalm.lang;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.highlighter.PhpHighlightingData;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.psalm.types.PsalmAliasTypeProvider;
import org.jetbrains.annotations.NotNull;

public class PsalmAnnotator implements Annotator {
  @Override
  public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
    if (PhpPsiUtil.isOfType(element, PhpDocTokenTypes.DOC_IDENTIFIER)) {
      PsiElement parent = element.getParent();
      if (parent instanceof PhpDocTag && PsalmAliasTypeProvider.PSALM_TYPE_TAG_NAME.equals(((PhpDocTag)parent).getName())) {
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
          .range(element)
          .textAttributes(PhpHighlightingData.DOC_IDENTIFIER)
          .create();
      }
    }
  }
}
