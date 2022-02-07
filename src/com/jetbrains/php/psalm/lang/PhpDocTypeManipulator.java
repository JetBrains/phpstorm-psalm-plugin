package com.jetbrains.php.psalm.lang;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PhpDocTypeManipulator extends AbstractElementManipulator<PhpDocType> {
  @Override
  public @Nullable PhpDocType handleContentChange(@NotNull PhpDocType element,
                                                  @NotNull TextRange range,
                                                  String newContent) throws IncorrectOperationException {
    throw new IncorrectOperationException();
  }
}
