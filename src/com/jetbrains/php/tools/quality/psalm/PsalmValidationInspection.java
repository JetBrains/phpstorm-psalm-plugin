package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.util.NlsSafe;
import com.jetbrains.php.tools.quality.QualityToolValidationInspection;
import org.jetbrains.annotations.NotNull;

import static com.jetbrains.php.tools.quality.psalm.PsalmConfigurationBaseManager.PSALM;

@SuppressWarnings("InspectionDescriptionNotFoundInspection")
public class PsalmValidationInspection extends QualityToolValidationInspection<PsalmValidationInspection> {

  @Override
  protected @NotNull PsalmAnnotatorProxy getAnnotator() {
    return PsalmAnnotatorProxy.INSTANCE;
  }

  @Override
  public @NlsSafe String getToolName() {
    return PSALM;
  }

}
