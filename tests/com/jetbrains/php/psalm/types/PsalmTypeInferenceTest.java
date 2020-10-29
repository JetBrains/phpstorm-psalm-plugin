package com.jetbrains.php.psalm.types;

import com.jetbrains.php.codeInsight.PhpTypeInferenceTestCase;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import org.jetbrains.annotations.NotNull;

public class PsalmTypeInferenceTest extends PhpTypeInferenceTestCase {
  public static final String TEST_DATA_HOME = "/phpstorm/psalm/testData/";

  @Override
  protected @NotNull String getTestDataHome() {
    return TEST_DATA_HOME;
  }

  @Override
  protected String getFixtureTestDataFolder() {
    return "codeInsight/typeInference";
  }

  public void testGenericArray$simpleKey() {
    doTypeTest(PhpType.STRING.pluralise());
  }

  public void testGenericArray$simpleValue() {
    doTypeTest(PhpType.STRING.pluralise());
  }

  public void testGenericArray$nonEmptyArray() {
    doTypeTest(PhpType.STRING.pluralise());
  }

  public void testGenericArray$nestedKey() {
    doTypeTest(PhpType.STRING.pluralise(2));
  }

  public void testGenericArray$nestedValue() {
    doTypeTest(PhpType.STRING.pluralise(2));
  }

  public void testGenericArray$nestedValueWithKey() {
    doTypeTest(PhpType.STRING.pluralise(2));
  }

  public void testGenericArray$union() {
    doTypeTest(PhpType.or(PhpType.STRING, PhpType.INT).pluralise());
  }

  public void testGenericArray$nonArray() {
    doTypeTest("\\nonArray"); // no particular guarantees here
  }

  public void testGenericArray$nestedClassString() {
    doTypeTest(PhpType.STRING.pluralise(2));
  }

  public void testPsalmParam() {
    doTypeTest(PhpType.STRING);
  }

  public void testPsalmReturn() {
    doTypeTest(PhpType.INT, PhpType.STRING);
  }

  public void testPsalmVarField() {
    doTypeTest(PhpType.FLOAT);
  }

  public void testPsalmVarVar() {
    doTypeTest(PhpType.INT.pluralise());
  }

  public void testClassString() {
    doTypeTest(PhpType.STRING);
  }

  public void testCallableString() {
    doTypeTest(PhpType.STRING);
  }

  public void testNumericString() {
    doTypeTest(PhpType.STRING);
  }

  public void testTraitString() {
    doTypeTest(PhpType.STRING);
  }

  public void testScalar() {
    assertEquals(getActualType().getTypes(), PhpType.SCALAR.getTypes());
  }

  public void testNumeric() {
    doTypeTest(PhpType.STRING, PhpType.INT, PhpType.FLOAT);
  }

  public void testArrayKey() {
    doTypeTest(PhpType.NUMERIC);
  }

  public void testTypesFromExtendedClassConstants() {
    doTypeTest(PhpType.INT);
  }

  public void testTypesFromExtendedClassConstantsWildcard() {
    doTypeTest(PhpType.INT, PhpType.STRING);
  }

  public void testFunctionTemplate() {
    doTypeTest(PhpType.INT, PhpType.MIXED);
  }

  public void testMethodTemplate() {
    doTypeTest(PhpType.INT, PhpType.MIXED);
  }

  public void testClassTemplate() {
    doTypeTest(PhpType.STRING, PhpType.INT, PhpType.MIXED);
  }

  public void testPsalmTagsTemplate() {
    doTypeTest(PhpType.INT, PhpType.MIXED);
  }

  public void testWithoutTemplate() {
    doTypeTest(new PhpType().add("\\T"));
  }

  public void testClosure() {
    doTypeTest(PhpType.CLOSURE);
  }

  public void testIntArrayKey() {
    doTypeTest(PhpType.INT);
  }

  public void testStringArrayKey() {
    doTypeTest(PhpType.STRING);
  }

  public void testUnknownArrayKey() {
    doTypeTest(PhpType.NUMERIC);
  }

  public void testScalarGeneric() {
    doTypeTest(PhpType.INT, PhpType.MIXED);
  }

  public void testPluralClassStringWithArgument() {
    doTypeTest(PhpType.STRING.pluralise());
  }

  public void testPluralClassString() {
    doTypeTest(PhpType.STRING.pluralise());
  }

  public void testPositiveInt() {
    doTypeTest(PhpType.INT);
  }

  public void testNonEmptyList() {
    doTypeTest(PhpType.STRING);
  }

  public void testNonEmptyString() {
    doTypeTest(PhpType.STRING);
  }

  public void testIterableKey() {
    doTypeTest("\\Foo");
  }

  public void testAdvancedCallable() {
    myFixture.addFileToProject("a.php", "<?php\n" +
                                        "class Foo {}\n" +
                                        "/**\n" +
                                        " * @return Closure(bool, int|string, $a int) : int|Foo\n" +
                                        " */\n" +
                                        "function a(): Closure{\n" +
                                        "\n" +
                                        "}\n" +
                                        "\n" +
                                        "function b(){\n" +
                                        "    return a();\n" +
                                        "}");
    doTypeTest(new PhpType().add("\\Foo").add(PhpType.MIXED).add(PhpType.INT));
  }

  public void testAdvancedCallableInSameFile() {
    doTypeTest(new PhpType().add("\\Foo").add(PhpType.MIXED).add(PhpType.INT));
  }
}
