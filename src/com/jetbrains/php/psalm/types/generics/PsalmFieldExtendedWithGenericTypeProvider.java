package com.jetbrains.php.psalm.types.generics;

import com.jetbrains.php.lang.psi.resolve.types.PhpTypeSignatureKey;
import org.jetbrains.annotations.NotNull;

public class PsalmFieldExtendedWithGenericTypeProvider extends PsalmBaseExtendedWithGenericTypeProvider {
  @Override
  protected @NotNull PhpTypeSignatureKey getSignatureKey() {
    return PhpTypeSignatureKey.FIELD;
  }
}
