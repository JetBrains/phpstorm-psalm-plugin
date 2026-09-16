package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.extensions.ExtensionPointName;
import com.jetbrains.php.tools.quality.QualityToolConfigurationProvider;
import org.jetbrains.annotations.Nullable;

import static com.jetbrains.php.tools.quality.psalm.PsalmConfigurationBaseManager.PSALM;

public abstract class PsalmConfigurationProvider extends QualityToolConfigurationProvider<PsalmConfiguration> {
  private static final ExtensionPointName<PsalmConfigurationProvider> EP_NAME =
    ExtensionPointName.create("com.jetbrains.php.tools.quality.Psalm.PsalmConfigurationProvider");

  public static @Nullable PsalmConfigurationProvider getInstances() {
    return getSingleInstance(EP_NAME, PSALM);
  }
}
