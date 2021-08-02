package com.jetbrains.php.psalm.lang.inspections;

import com.jetbrains.php.PhpInspectionTestCase;
import com.jetbrains.php.psalm.types.PsalmTypeInferenceTest;
import org.jetbrains.annotations.NotNull;

public class PsalmIncompatibleTypeOfArrayKeyInspectionTest extends PhpInspectionTestCase {
  @Override
  protected @NotNull String getTestDataHome() {
    return PsalmTypeInferenceTest.TEST_DATA_HOME;
  }

  @Override
  protected String getFixtureTestDataFolder() {
    return "codeInsight/inspections/incompatibleTypeOfArrayKey";
  }

  public void testBase() {
    doInspectionTest(PsalmIncompatibleTypeOfArrayKeyInspection.class);
  }
  
  public void testList() {
    doInspectionTest(PsalmIncompatibleTypeOfArrayKeyInspection.class);
  }
}