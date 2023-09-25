package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.jetbrains.php.composer.actions.log.ComposerLogMessageBuilder;
import org.jetbrains.annotations.NotNull;

public class PsalmOpenSettingsProvider extends ComposerLogMessageBuilder.Settings {
  public static final PsalmOpenSettingsProvider PSALM_OPEN_SETTINGS_PROVIDER = new PsalmOpenSettingsProvider();

  public PsalmOpenSettingsProvider() {super("\u200C");}

  @Override
  public void show(@NotNull Project project) {
    ShowSettingsUtil.getInstance().showSettingsDialog(project, PsalmBundle.message("configurable.quality.tool.psalm"));
  }
}