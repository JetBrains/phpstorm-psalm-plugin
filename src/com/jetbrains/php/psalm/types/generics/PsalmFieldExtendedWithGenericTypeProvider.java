package com.jetbrains.php.psalm.types.generics;

import com.jetbrains.php.lang.psi.resolve.types.PhpCharBasedTypeKey;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeSignatureKey;
import org.jetbrains.annotations.NotNull;

public class PsalmFieldExtendedWithGenericTypeProvider extends PsalmBaseExtendedWithGenericTypeProvider {

  private static final PhpCharBasedTypeKey FIELD = new PhpCharBasedTypeKey() {
    @Override
    public char getKey() {
      return PhpTypeSignatureKey.FIELD.getKey();
    }
  };

  @Override
  protected @NotNull PhpCharBasedTypeKey getSignatureKey() {
    return FIELD;
  }
}
