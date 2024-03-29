package com.jetbrains.php.psalm.quality.tools;

import com.intellij.openapi.util.Pair;
import com.intellij.testFramework.fixtures.CodeInsightFixtureTestCase;
import com.jetbrains.php.tools.quality.psalm.PsalmConfigurableForm;
import com.jetbrains.php.tools.quality.psalm.PsalmConfiguration;

public class PsalmVersionValidatorTest extends CodeInsightFixtureTestCase {


  public void testSimple(){
    final String STRING = "Psalm 3.11.4@58e1d8e68e5098bf4fbfdfb420c38d563f882549";
    final Pair<Boolean, String> result = new PsalmConfigurableForm<>(myFixture.getProject(), new PsalmConfiguration())
      .validateMessage(STRING);
    assertTrue(result.first);
    assertEquals("OK, " + STRING, result.second);
  }

  public void testDevMaster(){
    final String message = "Psalm dev-master@2ec76f01c2fe40aab288b34c20089c82ddf7bac6";
    final Pair<Boolean, String> result = new PsalmConfigurableForm<>(myFixture.getProject(), new PsalmConfiguration())
      .validateMessage(message);
    assertTrue(result.first);
    assertEquals("OK, " + message, result.second);
  }

  public void testVPrefix(){
    final String message = "Psalm v4.15.0@a1b5e489e6fcebe40cb804793d964e99fc347820";
    final Pair<Boolean, String> result = new PsalmConfigurableForm<>(myFixture.getProject(), new PsalmConfiguration())
      .validateMessage(message);
    assertTrue(result.first);
    assertEquals("OK, " + message, result.second);
  }
}
