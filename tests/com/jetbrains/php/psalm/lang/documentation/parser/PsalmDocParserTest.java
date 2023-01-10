package com.jetbrains.php.psalm.lang.documentation.parser;

import com.jetbrains.php.lang.parser.PhpParserTestCase;
import com.jetbrains.php.psalm.types.PsalmTypeInferenceTest;

public class PsalmDocParserTest extends PhpParserTestCase {

  public static final String DATA_PATH = PsalmTypeInferenceTest.TEST_DATA_HOME + "parser";

  @Override
  protected String getDataPath() {
    return DATA_PATH;
  }

  public void test$Param() throws Throwable {
    doTest();
  }

  public void test$Use() throws Throwable {
    doTest();
  }

  public void test$Return() throws Throwable {
    doTest();
  }

  public void test$Var() throws Throwable {
    doTest();
  }

  public void test$ExtendedConstantsInPhpDocType() throws Throwable {
    doTest();
  }

  public void test$ArrayShape() throws Throwable {
    doTest();
  }

  public void test$IntRanges() throws Throwable {
    doTest();
  }

  public void test$IntInGenericArray() throws Throwable {
    doTest();
  }

  public void test$PipeInNestedGenericArray() throws Throwable {
    doTest();
  }

  public void test$Callable() throws Throwable {
    doTest();
  }

  public void test$Template() throws Throwable {
    doTest();
  }

  public void test$ConditionalType() throws Throwable {
    doTest();
  }

  public void test$StaticInConditionalType() throws Throwable {
    doTest();
  }

  public void test$TemplateInConditionalType() throws Throwable {
    doTest();
  }

  public void test$MultilineConditionalType() throws Throwable {
    doTest();
  }

  public void test$LiteralType() throws Throwable {
    doTest();
  }

  public void test$QuotedKeysMultiple() throws Throwable {
    doTest();
  }

  public void test$QuotedKeysNullable() throws Throwable {
    doTest();
  }

  public void test$Inheritance() throws Throwable {
    doTest();
  }
}
