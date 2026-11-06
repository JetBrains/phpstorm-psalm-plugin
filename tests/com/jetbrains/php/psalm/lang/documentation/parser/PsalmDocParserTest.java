package com.jetbrains.php.psalm.lang.documentation.parser;

import com.intellij.openapi.project.Project;
import com.intellij.testFramework.junit5.TestApplication;
import com.intellij.testFramework.junit5.fixture.TestFixture;
import com.jetbrains.php.lang.parser.PhpFileSetParserTestFixture;
import com.jetbrains.php.psalm.types.PsalmTypeInferenceTest;
import org.junit.jupiter.api.Test;

import static com.intellij.testFramework.junit5.fixture.FixturesKt.projectFixture;
import static com.jetbrains.php.lang.parser.PhpFileSetParserTestFixture.phpFileSetParserFixture;

@TestApplication
public class PsalmDocParserTest {

  public static final String DATA_PATH = PsalmTypeInferenceTest.TEST_DATA_HOME + "parser";

  private static final TestFixture<Project> projectFixture = projectFixture();

  private final TestFixture<PhpFileSetParserTestFixture> parser = phpFileSetParserFixture(projectFixture, DATA_PATH);

  @Test
  public void test$Param() {
    parser.get().doTest();
  }

  @Test
  public void test$Use() {
    parser.get().doTest();
  }

  @Test
  public void test$Return() {
    parser.get().doTest();
  }

  @Test
  public void test$Var() {
    parser.get().doTest();
  }

  @Test
  public void test$ExtendedConstantsInPhpDocType() {
    parser.get().doTest();
  }

  @Test
  public void test$ArrayShape() {
    parser.get().doTest();
  }

  @Test
  public void test$IntRanges() {
    parser.get().doTest();
  }

  @Test
  public void test$IntInGenericArray() {
    parser.get().doTest();
  }

  @Test
  public void test$PipeInNestedGenericArray() {
    parser.get().doTest();
  }

  @Test
  public void test$Callable() {
    parser.get().doTest();
  }

  @Test
  public void test$Template() {
    parser.get().doTest();
  }

  @Test
  public void test$ConditionalType() {
    parser.get().doTest();
  }

  @Test
  public void test$StaticInConditionalType() {
    parser.get().doTest();
  }

  @Test
  public void test$TemplateInConditionalType() {
    parser.get().doTest();
  }

  @Test
  public void test$MultilineConditionalType() {
    parser.get().doTest();
  }

  @Test
  public void test$LiteralType() {
    parser.get().doTest();
  }

  @Test
  public void test$QuotedKeysMultiple() {
    parser.get().doTest();
  }

  @Test
  public void test$QuotedKeysNullable() {
    parser.get().doTest();
  }

  @Test
  public void test$Inheritance() {
    parser.get().doTest();
  }

  @Test
  public void test$Method() {
    parser.get().doTest();
  }

  @Test
  public void test$Type() {
    parser.get().doTest();
  }

  @Test
  public void test$Taint() {
    parser.get().doTest();
  }

  @Test
  public void test$PsalmInheritors() {
    parser.get().doTest();
  }

  @Test
  public void test$PsalmUnsealedArrayShape() {
    parser.get().doTest();
  }
}
