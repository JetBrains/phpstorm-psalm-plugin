package com.jetbrains.php.psalm.completion;

import com.jetbrains.php.fixtures.PhpCompletionTestCase;
import com.jetbrains.php.psalm.types.PsalmTypeInferenceTest;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PsalmCompletionTest extends PhpCompletionTestCase {

  @Override
  protected @NotNull String getTestDataHome() {
    return PsalmTypeInferenceTest.TEST_DATA_HOME;
  }

  @Override
  protected String getFixtureTestDataFolder() {
    return "completion";
  }

  public void testCustomDocTypes() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "class-string"
      , "callable-string"
      , "numeric-string"
      , "scalar"
      , "numeric"
      , "array-key"
    );
  }
}
