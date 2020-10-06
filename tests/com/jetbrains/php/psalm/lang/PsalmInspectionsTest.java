package com.jetbrains.php.psalm.lang;

import com.jetbrains.php.PhpInspectionTestCase;
import com.jetbrains.php.lang.inspections.PhpUndefinedClassInspection;
import com.jetbrains.php.psalm.types.PsalmTypeInferenceTest;
import org.jetbrains.annotations.NotNull;

public class PsalmInspectionsTest extends PhpInspectionTestCase {
  @Override
  protected @NotNull String getTestDataHome() {
    return PsalmTypeInferenceTest.TEST_DATA_HOME;
  }

  @Override
  protected String getFixtureTestDataFolder() {
    return "codeInsight/inspections";
  }

  public void testUndefinedClassExtendedScalar() {
    doInspectionTest(PhpUndefinedClassInspection.class);
  }

  public void testUndefinedClassArray() {
    doInspectionTest(PhpUndefinedClassInspection.class);
  }

  public void testUndefinedClassTemplate() {
    doInspectionTest(PhpUndefinedClassInspection.class);
  }

  public void testDocRefInsideDocType() {
    doInspectionTest(true);
  }
}
