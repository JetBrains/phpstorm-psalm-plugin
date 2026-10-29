package com.jetbrains.php.psalm.lang;

import com.intellij.codeHighlighting.HighlightDisplayLevel;
import com.intellij.openapi.project.Project;
import com.intellij.testFramework.junit5.TestApplication;
import com.intellij.testFramework.junit5.fixture.TestFixture;
import com.jetbrains.php.PhpInspectionTestFixture;
import com.jetbrains.php.config.PhpLanguageLevel;
import com.jetbrains.php.fixtures.PhpTestDataLayout;
import com.jetbrains.php.lang.inspections.PhpUndefinedClassInspection;
import com.jetbrains.php.lang.inspections.PhpUnnecessaryFullyQualifiedNameInspection;
import com.jetbrains.php.lang.inspections.controlFlow.PhpUnreachableStatementInspection;
import com.jetbrains.php.lang.inspections.deadcode.PhpUnusedDeclarationInspection;
import com.jetbrains.php.lang.inspections.phpdoc.PhpDocDuplicateTypeInspection;
import com.jetbrains.php.lang.inspections.phpdoc.PhpDocSignatureInspection;
import com.jetbrains.php.lang.inspections.phpdoc.PhpDocSignatureIsNotCompleteInspection;
import com.jetbrains.php.lang.inspections.phpdoc.PhpRedundantDocCommentInspection;
import com.jetbrains.php.lang.inspections.phpdoc.PhpRedundantVariableDocTypeInspection;
import com.jetbrains.php.lang.inspections.phpdoc.PhpVarTagWithoutVariableNameInspection;
import com.jetbrains.php.psalm.lang.inspections.PsalmAdvanceCallableParamsInspection;
import com.jetbrains.php.psalm.types.PsalmTypeInferenceTest;
import org.junit.jupiter.api.Test;

import static com.jetbrains.php.PhpInspectionTestFixture.phpInspectionFixture;
import static com.jetbrains.php.fixtures.PhpCodeInsightTestFixture.doLanguageLevelTest;
import static com.jetbrains.php.fixtures.PhpTestFixtures.phpProjectFixture;

@TestApplication
public class PsalmInspectionsTest {
  private static final TestFixture<Project> projectFixture = phpProjectFixture();
  private final TestFixture<PhpInspectionTestFixture> php =
    phpInspectionFixture(projectFixture, new PhpTestDataLayout("codeInsight/inspections").withHome(PsalmTypeInferenceTest.TEST_DATA_HOME));

  @Test
  public void testUndefinedClassExtendedScalar() {
    php.get().doInspectionTest(PhpUndefinedClassInspection.class);
  }

  @Test
  public void testUndefinedClassArray() {
    php.get().doInspectionTest(PhpUndefinedClassInspection.class);
  }

  @Test
  public void testUndefinedClassClosure() {
    php.get().doInspectionTest(PhpUndefinedClassInspection.class);
  }

  @Test
  public void testUndefinedClassTemplate() {
    php.get().doInspectionTest(PhpUndefinedClassInspection.class);
  }

  @Test
  public void testUndefinedClassPsalmType() {
    php.get().doInspectionTest(PhpUndefinedClassInspection.class);
  }

  @Test
  public void testUndefinedClassNoReturn() {
    php.get().doInspectionTest(PhpUndefinedClassInspection.class);
  }

  @Test
  public void testUndefinedClassArrayValueOfBackedEnum() {
    doLanguageLevelTest(php.get().getProject(), PhpLanguageLevel.PHP810, () -> php.get().doInspectionTest(PhpUndefinedClassInspection.class));
  }

  @Test
  public void testDocRefInsideDocType() {
    php.get().doInfoTest();
  }

  @Test
  public void testTypeAlreadyExists() {
    php.get().doInspectionTest(PhpDocDuplicateTypeInspection.class);
  }

  @Test
  public void testNoReturn() {
    php.get().doInspectionTest(PhpUnreachableStatementInspection.class);
  }

  @Test
  public void testAdvancedCallableParams() {
    php.get().addPhpFileToProject("a.php", "<?php\nfunction ff(): int {}");
    php.get().doInspectionTest(PsalmAdvanceCallableParamsInspection.class);
  }

  @Test
  public void testPartialFunctionApplicationCallableParams() {
    doLanguageLevelTest(php.get().getProject(), PhpLanguageLevel.PHP860,
                        () -> php.get().doInspectionTest(PsalmAdvanceCallableParamsInspection.class));
  }

  @Test
  public void testDocSignatureExtendedStringDocType() {
    php.get().doInspectionTest(PhpDocSignatureInspection.class);
  }

  @Test
  public void testArrayShapeExtendsFunctionSignature() {
    php.get().doInspectionTest(false, HighlightDisplayLevel.WARNING, PhpDocSignatureIsNotCompleteInspection.class, PhpRedundantDocCommentInspection.class);
  }

  @Test
  public void testGenericsExtendFunctionSignature() {
    php.get().doInspectionTest(false, HighlightDisplayLevel.WARNING, PhpDocSignatureIsNotCompleteInspection.class, PhpRedundantDocCommentInspection.class);
  }

  @Test
  public void testSpecialTypesExtendSignature() {
    php.get().doInspectionTest(false, HighlightDisplayLevel.WARNING, PhpDocSignatureIsNotCompleteInspection.class, PhpRedundantDocCommentInspection.class);
  }

  @Test
  public void testArrayShape() {
    php.get().doInspectionTest(PhpRedundantVariableDocTypeInspection.class);
  }

  @Test
  public void testGenericsExtendVarType() {
    php.get().doInspectionTest(PhpRedundantVariableDocTypeInspection.class);
  }

  @Test
  public void testPsalmTraceVar() {
    php.get().doInfoTest();
  }

  @Test
  public void testReturnGenericTemplate() {
    php.get().doInspectionTest(PsalmAdvanceCallableParamsInspection.class);
  }

  @Test
  public void testVariadicClosureParams() {
    php.get().doInspectionTest(PsalmAdvanceCallableParamsInspection.class);
  }

  @Test
  public void testVariadicArrowFunctionParams() {
    php.get().doInspectionTest(PsalmAdvanceCallableParamsInspection.class);
  }

  @Test
  public void testParamTagInsideCallable() {
    php.get().doInspectionTest(PhpVarTagWithoutVariableNameInspection.class);
  }

  @Test
  public void testFindWildcardsUsagesForConstants() {
    php.get().doUnfairInspectionTest(PhpUnusedDeclarationInspection.class);
  }

  @Test
  public void testFindWildcardsClassReferenceUnnecessaryFQN() {
    php.get().doInspectionTest(PhpUnnecessaryFullyQualifiedNameInspection.class);
  }

  @Test
  public void testLiteralTypeDuplicate() {
    php.get().doInspectionTest(PhpDocDuplicateTypeInspection.class);
  }

  @Test
  public void testLiteralTypeUnresolvedClass() {
    php.get().doInspectionTest(PhpUndefinedClassInspection.class);
  }

  @Test
  public void testNonEmptyListWithoutTypes() {
    php.get().doInspectionTest(PhpUndefinedClassInspection.class);
  }

  @Test
  public void testNonEmptyArrayAsConditionForType() {
    php.get().doInspectionTest(PhpUndefinedClassInspection.class);
  }
}
