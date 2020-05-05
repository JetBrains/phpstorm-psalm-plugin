package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.project.Project;
import com.jetbrains.php.composer.actions.log.ComposerLogMessageBuilder;
import com.jetbrains.php.tools.quality.QualityToolConfigurationManager;
import com.jetbrains.php.tools.quality.QualityToolsComposerConfig;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import static com.jetbrains.php.composer.actions.log.ComposerLogMessageBuilder.Settings.PHPSTAN;

public class PsalmComposerConfig extends QualityToolsComposerConfig<PsalmConfiguration, PsalmValidationInspection> {
  @NonNls private static final String PACKAGE = "vimeo/psalm";
  @NonNls private static final String RELATIVE_PATH = "bin/psalm";
  private static final PsalmValidationInspection PHP_STAN_VALIDATION_INSPECTION = new PsalmValidationInspection();


  public PsalmComposerConfig() {
    super(PACKAGE, RELATIVE_PATH);
  }

  @Override
  protected ComposerLogMessageBuilder.Settings getQualityToolsInspectionSettings() {
    return null;
  }

  @Override
  public PsalmValidationInspection getQualityInspection() {
    return PHP_STAN_VALIDATION_INSPECTION;
  }

  @Override
  public ComposerLogMessageBuilder.Settings getSettings() {
    return PHPSTAN;
  }

  @Override
  protected boolean applyRulesetFromComposer(@NotNull Project project, PsalmConfiguration configuration) {
    return true;
  }

  @NotNull
  @Override
  protected QualityToolConfigurationManager<PsalmConfiguration> getConfigurationManager(@NotNull Project project) {
    return PsalmConfigurationManager.getInstance(project);
  }
}