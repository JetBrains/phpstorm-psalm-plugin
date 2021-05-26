package com.jetbrains.php.psalm.lang;

import com.jetbrains.php.fixtures.PhpCompletionTestCase;
import com.jetbrains.php.psalm.types.PsalmTypeInferenceTest;
import org.jetbrains.annotations.NotNull;

public class PsalmCompletionTest extends PhpCompletionTestCase {
  @Override
  protected @NotNull String getTestDataHome() {
    return PsalmTypeInferenceTest.TEST_DATA_HOME;
  }

  @Override
  protected String getFixtureTestDataFolder() {
    return "completion";
  }

  public void testArrayShape() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "name", "age");
  }
}
