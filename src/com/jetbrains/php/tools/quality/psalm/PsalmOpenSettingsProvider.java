package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.project.Project;
import com.jetbrains.php.composer.actions.log.ComposerLogMessageBuilder;
import com.jetbrains.php.tools.quality.QualityToolConfigurableList;
import com.jetbrains.php.tools.quality.QualityToolType;
import com.jetbrains.php.ui.PhpUiUtil;
import org.jetbrains.annotations.NotNull;

public class PsalmOpenSettingsProvider extends ComposerLogMessageBuilder.Settings {
  public static final PsalmOpenSettingsProvider PSALM_OPEN_SETTINGS_PROVIDER = new PsalmOpenSettingsProvider();

  public PsalmOpenSettingsProvider() {super("\u200C");}

  @Override
  public void show(@NotNull Project project) {
    PhpUiUtil.editConfigurable(project, new QualityToolConfigurableList<>(project, PsalmQualityToolType.INSTANCE, null) {
      @Override
      protected QualityToolType<PsalmConfiguration> getQualityToolType() {
        return PsalmQualityToolType.INSTANCE;
      }
    });
  }
}