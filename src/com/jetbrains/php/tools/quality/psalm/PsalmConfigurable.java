package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.jetbrains.php.tools.quality.QualityToolConfigurationComboBox;
import com.jetbrains.php.tools.quality.QualityToolProjectConfigurableForm;
import com.jetbrains.php.tools.quality.QualityToolType;
import com.jetbrains.php.tools.quality.QualityToolsOptionsPanel;
import org.jetbrains.annotations.NotNull;

public class PsalmConfigurable extends QualityToolProjectConfigurableForm implements Configurable.NoScroll {

  public PsalmConfigurable(@NotNull Project project) {
    super(project);
  }

  @Override
  public @NotNull String getId() {
    return "settings.php.quality.tools.psalm";
  }
  @Override
  protected QualityToolsOptionsPanel getQualityToolOptionPanel(QualityToolConfigurationComboBox configurationBox, Runnable validate) {
    return new PsalmOptionsPanel(myProject, configurationBox, validate);
  }

  @Override
  protected @NotNull QualityToolType<PsalmConfiguration> getQualityToolType() {
    return PsalmQualityToolType.INSTANCE;
  }
}
