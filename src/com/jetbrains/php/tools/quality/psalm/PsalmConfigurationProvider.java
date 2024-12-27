package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.jetbrains.php.tools.quality.QualityToolConfigurationProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class PsalmConfigurationProvider extends QualityToolConfigurationProvider<PsalmConfiguration> {
  private static final Logger LOG = Logger.getInstance(PsalmConfigurationProvider.class);
  private static final ExtensionPointName<PsalmConfigurationProvider> EP_NAME =
    ExtensionPointName.create("com.jetbrains.php.tools.quality.Psalm.PsalmConfigurationProvider");

  public static @Nullable PsalmConfigurationProvider getInstances() {
    final @NotNull List<PsalmConfigurationProvider> extensions = EP_NAME.getExtensionList();
    if (extensions.size() > 1) {
      LOG.error("Several providers for remote Psalm configuration was found");
    }
    return extensions.isEmpty() ? null : extensions.get(0);
  }
}
