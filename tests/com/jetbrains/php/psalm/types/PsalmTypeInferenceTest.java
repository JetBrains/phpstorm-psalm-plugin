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
}
