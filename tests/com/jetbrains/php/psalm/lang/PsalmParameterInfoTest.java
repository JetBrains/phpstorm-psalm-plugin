package com.jetbrains.php.psalm.lang;

import com.jetbrains.php.PhpParameterInfoTest;
import com.jetbrains.php.fixtures.PhpCodeInsightFixtureTestCase;
import com.jetbrains.php.psalm.types.PsalmTypeInferenceTest;
import org.jetbrains.annotations.NotNull;

public class PsalmParameterInfoTest extends PhpCodeInsightFixtureTestCase {
  @Override
  protected @NotNull String getTestDataHome() {
    return PsalmTypeInferenceTest.TEST_DATA_HOME;
  }

  @Override
  protected String getFixtureTestDataFolder() {
    return "parameterInfo";
  }

  public void testPsalmDocClosure() {
    doTest("bool, j: int");
  }

  public void testNativeClosure() {
    doTest("a: int|string, b: int");
  }

  private void doTest(String parameterInfo) {
    PsalmAdvancedClosureParameterInfoHandler handler = new PsalmAdvancedClosureParameterInfoHandler();
    configureByFile("php");
    PhpParameterInfoTest.doTest(getEditor(), getFile(), handler);
    assertEquals(parameterInfo, handler.getText());
  }
}
