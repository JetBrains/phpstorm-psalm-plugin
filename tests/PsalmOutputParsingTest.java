import com.jetbrains.php.fixtures.PhpHeavyCodeInsightFixtureTestCase;
import com.jetbrains.php.tools.quality.psalm.PsalmConfigurationManager;
import com.jetbrains.php.tools.quality.psalm.PsalmValidationInspection;
import org.jetbrains.annotations.NotNull;

public class PsalmOutputParsingTest extends PhpHeavyCodeInsightFixtureTestCase {
 
  public void testSimple() {
    PsalmConfigurationManager.getInstance(myFixture.getProject()).getLocalSettings().setToolPath("psalm"); // Dummy, needed to run annotator
    configureByFiles(getFileBeforeRelativePath().replace(".php", ".txt"));
    myFixture.enableInspections(PsalmValidationInspection.class);
    myFixture.testHighlighting(true, false, true);
  }

  @Override
  protected String getFixtureTestDataFolder() {
    return "output";
  }

  @NotNull
  @Override
  protected String getFileBeforeExtension() {
    return "php";
  }

  @Override
  protected String getBasePath() {
    return "/phpstorm/psalm/testData/output";
  }
}
