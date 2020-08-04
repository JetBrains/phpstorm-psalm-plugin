package com.jetbrains.php.tools.quality.psalm;

import com.jetbrains.php.tools.quality.QualityToolAnnotator;
import com.jetbrains.php.tools.quality.QualityToolValidationInspection;
import org.jetbrains.annotations.NotNull;

import static com.jetbrains.php.tools.quality.psalm.PsalmConfigurationBaseManager.PSALM;

@SuppressWarnings("InspectionDescriptionNotFoundInspection")
public class PsalmValidationInspection extends QualityToolValidationInspection {

  @NotNull
  @Override
  protected QualityToolAnnotator<PsalmValidationInspection> getAnnotator() {
    return PsalmAnnotatorProxy.INSTANCE;
  }

  @Override
  public String getToolName() {
    return PSALM;
  }

}
