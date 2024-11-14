package com.jetbrains.php.psalm.quality.tools;

import com.jetbrains.php.fixtures.PhpHeavyCodeInsightFixtureTestCase;
import com.jetbrains.php.tools.quality.psalm.PsalmConfigurationManager;
import com.jetbrains.php.tools.quality.psalm.PsalmGlobalInspection;

public class PsalmOutputParsingTest extends PhpHeavyCodeInsightFixtureTestCase {
 
  public void testSimple() {
    PsalmConfigurationManager.getInstance(myFixture.getProject()).getOrCreateLocalSettings().setToolPath("psalm"); // Dummy, needed to run annotator
    configureByFiles(getFileBeforeRelativePath().replace(".php", ".txt"));
    myFixture.enableInspections(new PsalmGlobalInspection());
    myFixture.testHighlighting(true, false, true);
  }

  @Override
  protected String getFixtureTestDataFolder() {
    return "output";
  }

  @Override
  protected String getBasePath() {
    return "/phpstorm/psalm/testData/output";
  }
}
