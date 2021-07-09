package com.jetbrains.php.psalm.types.generics;

import com.jetbrains.php.lang.psi.resolve.types.PhpCharBasedTypeKey;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeSignatureKey;
import org.jetbrains.annotations.NotNull;

public class PsalmMethodExtendedWithGenericTypeProvider extends PsalmBaseExtendedWithGenericTypeProvider {

  private static final PhpCharBasedTypeKey KEY = new PhpCharBasedTypeKey() {
    @Override
    public char getKey() {
      return PhpTypeSignatureKey.METHOD.getKey();
    }
  };

  @Override
  protected @NotNull PhpCharBasedTypeKey getSignatureKey() {
    return KEY;
  }
}
