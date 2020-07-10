package com.jetbrains.php.psalm.lang;

import com.intellij.codeInspection.InspectionProfileEntry;
import com.jetbrains.php.PhpFixesTestCase;
import com.jetbrains.php.lang.inspections.PhpFullyQualifiedNameUsageInspection;
import com.jetbrains.php.lang.inspections.PhpUnnecessaryFullyQualifiedNameInspection;
import com.jetbrains.php.psalm.types.PsalmTypeInferenceTest;
import org.jetbrains.annotations.NotNull;

public class PsalmFixesTest extends PhpFixesTestCase {
  @Override
  protected @NotNull String getTestDataHome() {
    return PsalmTypeInferenceTest.TEST_DATA_HOME;
  }

  @Override
  protected String getFixtureTestDataFolder() {
    return "codeInsight/fixes";
  }

  public void testNestedImport() {
    doTestQuickFix("Replace qualifier with an import", new PhpFullyQualifiedNameUsageInspection());
  }

  public void testNestedImportFQN() {
    doTestQuickFix("Remove unnecessary qualifier", new PhpUnnecessaryFullyQualifiedNameInspection());
  }
}
