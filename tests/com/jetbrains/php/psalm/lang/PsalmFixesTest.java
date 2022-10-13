package com.jetbrains.php.psalm.lang;

import com.jetbrains.php.PhpBundle;
import com.jetbrains.php.PhpFixesTestCase;
import com.jetbrains.php.lang.inspections.PhpFullyQualifiedNameUsageInspection;
import com.jetbrains.php.lang.inspections.PhpUnnecessaryFullyQualifiedNameInspection;
import com.jetbrains.php.lang.inspections.codeStyle.PhpPluralMixedCanBeReplacedWithArrayInspection;
import com.jetbrains.php.lang.inspections.phpdoc.PhpVarTagWithoutVariableNameInspection;
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

  public void testPluralMixed() {
    doTestQuickFix("Replace with 'array'", new PhpPluralMixedCanBeReplacedWithArrayInspection());
  }

  public void testPluralMixedWithKeys() {
    doCheckNoQuickFix("Replace with 'array'", new PhpPluralMixedCanBeReplacedWithArrayInspection());
  }

  public void testVarTagWithoutVariableName() {
    doTestQuickFix("Add '$a'", new PhpVarTagWithoutVariableNameInspection());
  }

  public void testFqnUsageInWildcard() {
    doTestQuickFix("Remove unnecessary qualifier", new PhpUnnecessaryFullyQualifiedNameInspection());
  }
}
