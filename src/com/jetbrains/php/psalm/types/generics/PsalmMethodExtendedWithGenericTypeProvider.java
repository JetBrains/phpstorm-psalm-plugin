package com.jetbrains.php.psalm.types.generics;

import com.jetbrains.php.lang.psi.resolve.types.PhpTypeSignatureKey;
import org.jetbrains.annotations.NotNull;

public class PsalmMethodExtendedWithGenericTypeProvider extends PsalmBaseExtendedWithGenericTypeProvider {
  @Override
  protected @NotNull PhpTypeSignatureKey getSignatureKey() {
    return PhpTypeSignatureKey.METHOD;
  }
}
