package com.jetbrains.php.psalm.types;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.PhpCustomDocTagTypeProvider;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

public class PsalmDocTagTypeProvider extends PhpCustomDocTagTypeProvider {
  private static final String PARAM = "@psalm-param";
  public static final String RETURN = "@psalm-return";
  private static final String VAR = "@psalm-var";

  @Override
  public char getKey() {
    return 'ᢓ';
  }

  @Override
  protected @NotNull String getParamTag() {
    return PARAM;
  }

  @Override
  protected @NotNull String getReturnTag() {
    return RETURN;
  }

  @Override
  protected @NotNull String getVarTag() {
    return VAR;
  }
}
