package com.jetbrains.php.psalm.types.generics;

import com.jetbrains.php.lang.psi.resolve.types.PhpTypeSignatureKey;

public class PsalmFieldExtendedWithGenericTypeProvider extends PsalmBaseExtendedWithGenericTypeProvider {
  @Override
  public char getKey() {
    return PhpTypeSignatureKey.FIELD.getKey();
  }
}
