package com.jetbrains.php.psalm.lang;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.project.Project;
import com.intellij.testFramework.junit5.TestApplication;
import com.intellij.testFramework.junit5.fixture.TestFixture;
import com.jetbrains.php.PhpParameterInfoTest;
import com.jetbrains.php.fixtures.PhpCodeInsightTestFixture;
import com.jetbrains.php.fixtures.PhpTestDataLayout;
import com.jetbrains.php.psalm.types.PsalmTypeInferenceTest;
import org.junit.jupiter.api.Test;

import static com.jetbrains.php.fixtures.PhpCodeInsightTestFixture.phpCodeInsightFixture;
import static com.jetbrains.php.fixtures.PhpTestFixtures.phpProjectFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestApplication
public class PsalmParameterInfoTest {
  private static final TestFixture<Project> projectFixture = phpProjectFixture();
  private final TestFixture<PhpCodeInsightTestFixture> php =
    phpCodeInsightFixture(projectFixture, new PhpTestDataLayout("parameterInfo").withHome(PsalmTypeInferenceTest.TEST_DATA_HOME));

  @Test
  public void testPsalmDocClosure() {
    doTest("bool, j: int");
  }

  @Test
  public void testNativeClosure() {
    doTest("a: int|string, b: int");
  }

  private void doTest(String parameterInfo) {
    PsalmAdvancedClosureParameterInfoHandler handler = new PsalmAdvancedClosureParameterInfoHandler();
    php.get().configureByFile("php");
    ReadAction.runBlocking(() -> PhpParameterInfoTest.doTest(php.get().getEditor(), php.get().getFixture().getFile(), handler));
    assertEquals(parameterInfo, handler.getText());
  }
}
