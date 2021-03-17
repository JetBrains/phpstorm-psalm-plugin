package com.jetbrains.php.psalm.types;

import com.jetbrains.php.lang.psi.resolve.types.PhpCustomDocTagTypeProvider;
import org.jetbrains.annotations.NotNull;

public class PsalmDocTagTypeProvider extends PhpCustomDocTagTypeProvider {
  private static final String PARAM = "@psalm-param";
  public static final String RETURN = "@psalm-return";
  private static final String VAR = "@psalm-var";

  @Override
  public char getKey() {
    return 'ᢓ';
  }

  @Override
  @NotNull
  public String getParamTag() {
    return PARAM;
  }

  @Override
  public @NotNull String getReturnTag() {
    return RETURN;
  }

  @Override
  public @NotNull String getVarTag() {
    return VAR;
  }
}
