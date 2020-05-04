package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.project.Project;
import com.jetbrains.php.tools.quality.QualityToolAddToIgnoredAction;
import com.jetbrains.php.tools.quality.QualityToolType;
import org.jetbrains.annotations.NotNull;

public class PsalmAddToIgnoredAction extends QualityToolAddToIgnoredAction {

  @Override
  protected @NotNull QualityToolType<PsalmConfiguration> getQualityToolType(Project project) {
    return PsalmQualityToolType.INSTANCE;
  }
}
