package com.jetbrains.php.tools.quality.psalm;

import com.jetbrains.php.tools.quality.QualityToolValidationInspection;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("InspectionDescriptionNotFoundInspection")
public class PsalmValidationInspection extends QualityToolValidationInspection<PsalmValidationInspection> {

  @Override
  protected @NotNull PsalmAnnotatorProxy getAnnotator() {
    return PsalmAnnotatorProxy.INSTANCE;
  }
}
