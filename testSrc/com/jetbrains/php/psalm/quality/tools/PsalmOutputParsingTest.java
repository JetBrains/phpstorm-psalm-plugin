package com.jetbrains.php.psalm.quality.tools;

import com.intellij.testFramework.fixtures.CodeInsightTestFixture;
import com.intellij.testFramework.junit5.TestApplication;
import com.intellij.testFramework.junit5.fixture.TestFixture;
import com.jetbrains.php.fixtures.PhpHeavyCodeInsightTestFixture;
import com.jetbrains.php.fixtures.PhpTestDataLayout;
import com.jetbrains.php.tools.quality.psalm.PsalmConfigurationManager;
import com.jetbrains.php.tools.quality.psalm.PsalmGlobalInspection;
import org.junit.jupiter.api.Test;

import static com.jetbrains.php.fixtures.PhpHeavyCodeInsightTestFixture.phpHeavyCodeInsightFixture;

@TestApplication
public class PsalmOutputParsingTest {
  private final TestFixture<PhpHeavyCodeInsightTestFixture> php =
    phpHeavyCodeInsightFixture(new PhpTestDataLayout("output").withHome("/phpstorm/psalm/testData/").withoutTestDir());

  @Test
  public void testSimple() {
    CodeInsightTestFixture myFixture = php.get().getFixture();
    PsalmConfigurationManager.getInstance(myFixture.getProject()).getOrCreateLocalSettings().setToolPath("psalm"); // Dummy, needed to run annotator
    php.get().configureByFiles(php.get().getFileBeforeRelativePath().replace(".php", ".txt"));
    myFixture.enableInspections(new PsalmGlobalInspection());
    myFixture.testHighlighting(true, false, true);
  }
}
