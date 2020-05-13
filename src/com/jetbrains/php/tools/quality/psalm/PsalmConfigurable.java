package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.jetbrains.php.tools.quality.QualityToolProjectConfigurableForm;
import com.jetbrains.php.tools.quality.QualityToolType;
import org.jetbrains.annotations.NotNull;

public class PsalmConfigurable extends QualityToolProjectConfigurableForm implements Configurable.NoScroll {

  public PsalmConfigurable(@NotNull Project project) {
    super(project);
  }

  @Override
  protected QualityToolType<PsalmConfiguration> getQualityToolType() {
    return PsalmQualityToolType.INSTANCE;
  }
}
