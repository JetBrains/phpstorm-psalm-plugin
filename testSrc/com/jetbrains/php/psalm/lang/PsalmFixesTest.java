package com.jetbrains.php.psalm.lang;

import com.intellij.openapi.project.Project;
import com.intellij.testFramework.junit5.TestApplication;
import com.intellij.testFramework.junit5.fixture.TestFixture;
import com.jetbrains.php.PhpFixesTestFixture;
import com.jetbrains.php.fixtures.PhpTestDataLayout;
import com.jetbrains.php.lang.inspections.PhpFullyQualifiedNameUsageInspection;
import com.jetbrains.php.lang.inspections.PhpUnnecessaryFullyQualifiedNameInspection;
import com.jetbrains.php.lang.inspections.codeStyle.PhpPluralMixedCanBeReplacedWithArrayInspection;
import com.jetbrains.php.lang.inspections.phpdoc.PhpVarTagWithoutVariableNameInspection;
import com.jetbrains.php.psalm.types.PsalmTypeInferenceTest;
import org.junit.jupiter.api.Test;

import static com.jetbrains.php.PhpFixesTestFixture.phpFixesFixture;
import static com.jetbrains.php.fixtures.PhpTestFixtures.phpProjectFixture;

@TestApplication
public class PsalmFixesTest {
  private static final TestFixture<Project> projectFixture = phpProjectFixture();
  private final TestFixture<PhpFixesTestFixture> php =
    phpFixesFixture(projectFixture, new PhpTestDataLayout("codeInsight/fixes").withHome(PsalmTypeInferenceTest.TEST_DATA_HOME));

  @Test
  public void testNestedImport() {
    php.get().doTestQuickFix("Replace qualifier with an import", new PhpFullyQualifiedNameUsageInspection());
  }

  @Test
  public void testNestedImportFQN() {
    php.get().doTestQuickFix("Remove unnecessary qualifier", new PhpUnnecessaryFullyQualifiedNameInspection());
  }

  @Test
  public void testPluralMixed() {
    php.get().doTestQuickFix("Replace with 'array'", new PhpPluralMixedCanBeReplacedWithArrayInspection());
  }

  @Test
  public void testPluralMixedWithKeys() {
    php.get().doCheckNoQuickFix("Replace with 'array'", new PhpPluralMixedCanBeReplacedWithArrayInspection());
  }

  @Test
  public void testVarTagWithoutVariableName() {
    php.get().doTestQuickFix("Add '$a'", new PhpVarTagWithoutVariableNameInspection());
  }

  @Test
  public void testFqnUsageInWildcard() {
    php.get().doTestQuickFix("Remove unnecessary qualifier", new PhpUnnecessaryFullyQualifiedNameInspection());
  }
}
