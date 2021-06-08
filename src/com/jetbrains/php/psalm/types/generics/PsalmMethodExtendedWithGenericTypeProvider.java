package com.jetbrains.php.psalm.types.generics;

import com.jetbrains.php.lang.psi.resolve.types.PhpTypeSignatureKey;

public class PsalmMethodExtendedWithGenericTypeProvider extends PsalmBaseExtendedWithGenericTypeProvider {
  @Override
  public char getKey() {
    return PhpTypeSignatureKey.METHOD.getKey();
  }
}
