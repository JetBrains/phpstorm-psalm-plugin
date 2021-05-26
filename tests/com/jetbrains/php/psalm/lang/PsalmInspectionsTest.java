package com.jetbrains.php.psalm.lang;

import com.jetbrains.php.PhpInspectionTestCase;
import com.jetbrains.php.lang.inspections.PhpUndefinedClassInspection;
import com.jetbrains.php.lang.inspections.controlFlow.PhpUnreachableStatementInspection;
import com.jetbrains.php.lang.inspections.phpdoc.PhpDocDuplicateTypeInspection;
import com.jetbrains.php.lang.inspections.phpdoc.PhpDocSignatureInspection;
import com.jetbrains.php.lang.inspections.phpdoc.PhpRedundantVariableDocTypeInspection;
import com.jetbrains.php.psalm.lang.inspections.PsalmAdvanceCallableParamsInspection;
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

  public void testUndefinedClassClosure() {
    doInspectionTest(PhpUndefinedClassInspection.class);
  }

  public void testUndefinedClassTemplate() {
    doInspectionTest(PhpUndefinedClassInspection.class);
  }

  public void testUndefinedClassPsalmType() {
    doInspectionTest(PhpUndefinedClassInspection.class);
  }

  public void testUndefinedClassNoReturn() {
    doInspectionTest(PhpUndefinedClassInspection.class);
  }

  public void testDocRefInsideDocType() {
    doInspectionTest(true);
  }

  public void testTypeAlreadyExists() {
    doInspectionTest(PhpDocDuplicateTypeInspection.class);
  }

  public void testNoReturn() {
    doInspectionTest(PhpUnreachableStatementInspection.class);
  }

  public void testAdvancedCallableParams() {
    addPhpFileToProject("a.php", "<?php\nfunction ff(): int {}");
    doInspectionTest(PsalmAdvanceCallableParamsInspection.class);
  }

  public void testDocSignatureExtendedStringDocType() {
    doInspectionTest(PhpDocSignatureInspection.class);
  }

  public void testArrayShape() {
    doInspectionTest(PhpRedundantVariableDocTypeInspection.class);
  }
}
