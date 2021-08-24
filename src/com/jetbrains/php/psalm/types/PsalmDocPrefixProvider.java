package com.jetbrains.php.psalm.types;

import com.jetbrains.php.lang.psi.resolve.types.PhpDocPrefixProvider;
import org.jetbrains.annotations.NotNull;

public class PsalmDocPrefixProvider implements PhpDocPrefixProvider {
  @Override
  public @NotNull String getPrefix() {
    return "@psalm-";
  }
}
