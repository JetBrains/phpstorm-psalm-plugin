package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.jetbrains.php.tools.quality.QualityToolProjectConfigurableForm;
import com.jetbrains.php.tools.quality.QualityToolType;
import com.jetbrains.php.tools.quality.QualityToolsOptionsPanel;
import org.jetbrains.annotations.NotNull;

public class PsalmConfigurable extends QualityToolProjectConfigurableForm implements Configurable.NoScroll {

  public PsalmConfigurable(@NotNull Project project) {
    super(project);
  }

  @Override
  protected QualityToolsOptionsPanel getQualityToolOptionPanel() {
    return new PsalmOptionsPanel(new PsalmGlobalInspection());
  }

  @Override
  protected QualityToolType<PsalmConfiguration> getQualityToolType() {
    return PsalmQualityToolType.INSTANCE;
  }
}
