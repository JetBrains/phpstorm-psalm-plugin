package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.jetbrains.php.tools.quality.QualityToolConfigurationProvider;
import org.jetbrains.annotations.Nullable;

public abstract class PsalmConfigurationProvider extends QualityToolConfigurationProvider<PsalmConfiguration> {
  private static final Logger LOG = Logger.getInstance(PsalmConfigurationProvider.class);
  private static final ExtensionPointName<PsalmConfigurationProvider> EP_NAME =
    ExtensionPointName.create("com.jetbrains.php.tools.quality.Psalm.PsalmConfigurationProvider");

  @Nullable
  public static PsalmConfigurationProvider getInstances() {
    final PsalmConfigurationProvider[] extensions = EP_NAME.getExtensions();
    if (extensions.length > 1) {
      LOG.error("Several providers for remote Psalm configuration was found");
    }
    return extensions.length == 0 ? null : extensions[0];
  }
}
