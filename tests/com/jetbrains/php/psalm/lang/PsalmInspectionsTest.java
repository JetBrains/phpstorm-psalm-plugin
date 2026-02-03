package com.jetbrains.php.psalm.lang;

import com.intellij.codeHighlighting.HighlightDisplayLevel;
import com.jetbrains.php.PhpInspectionTestCase;
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
    doInfoTest();
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

  public void testArrayShapeExtendsFunctionSignature() {
    doInspectionTest(false, HighlightDisplayLevel.WARNING, PhpDocSignatureIsNotCompleteInspection.class, PhpRedundantDocCommentInspection.class);
  }

  public void testGenericsExtendFunctionSignature() {
    doInspectionTest(false, HighlightDisplayLevel.WARNING, PhpDocSignatureIsNotCompleteInspection.class, PhpRedundantDocCommentInspection.class);
  }

  public void testSpecialTypesExtendSignature() {
    doInspectionTest(false, HighlightDisplayLevel.WARNING, PhpDocSignatureIsNotCompleteInspection.class, PhpRedundantDocCommentInspection.class);
  }

  public void testArrayShape() {
    doInspectionTest(PhpRedundantVariableDocTypeInspection.class);
  }

  public void testGenericsExtendVarType() {
    doInspectionTest(PhpRedundantVariableDocTypeInspection.class);
  }

  public void testPsalmTraceVar() {
    doInfoTest();
  }

  public void testReturnGenericTemplate() {
    doInspectionTest(PsalmAdvanceCallableParamsInspection.class);
  }

  public void testParamTagInsideCallable() {
    doInspectionTest(PhpVarTagWithoutVariableNameInspection.class);
  }

  public void testFindWildcardsUsagesForConstants() {
    doUnfairInspectionTest(PhpUnusedDeclarationInspection.class);
  }

  public void testFindWildcardsClassReferenceUnnecessaryFQN() {
    doInspectionTest(PhpUnnecessaryFullyQualifiedNameInspection.class);
  }

  public void testLiteralTypeDuplicate() {
    doInspectionTest(PhpDocDuplicateTypeInspection.class);
  }

  public void testLiteralTypeUnresolvedClass() {
    doInspectionTest(PhpUndefinedClassInspection.class);
  }

  public void testNonEmptyListWithoutTypes() {
    doInspectionTest(PhpUndefinedClassInspection.class);
  }

  public void testNonEmptyArrayAsConditionForType() {
    doInspectionTest(PhpUndefinedClassInspection.class);
  }
}
