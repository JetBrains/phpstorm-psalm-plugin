package com.jetbrains.php.psalm.quality.tools;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;
import com.intellij.testFramework.junit5.TestApplication;
import com.intellij.testFramework.junit5.fixture.TestFixture;
import com.jetbrains.php.tools.quality.psalm.PsalmComposerConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static com.intellij.platform.testFramework.junit5.codeInsight.fixture.CodeInsightFixtureKt.codeInsightFixture;
import static com.intellij.platform.testFramework.junit5.codeInsight.fixture.CodeInsightFixtureKt.codeInsightProjectFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestApplication
public class PsalmConfigFileFromComposerTest {
  private final TestFixture<Project> projectFixture = codeInsightProjectFixture();
  private final TestFixture<CodeInsightTestFixture> codeInsightFixture = codeInsightFixture(projectFixture);

  private void doRulesetTest(@NotNull String content, @Nullable String expectedPath) {
    CodeInsightTestFixture myFixture = codeInsightFixture.get();
    myFixture.configureByText("composer.json", content);
    VirtualFile configFile = myFixture.getFile().getVirtualFile();
    assertEquals(expectedPath, new PsalmComposerConfig().getRuleset(configFile));
  }

  @Test
  public void testCFormat() {
    doRulesetTest("""
                    
                    {
                        "scripts": {
                            "psalm": "./vendor/bin/psalm -c src/psalm.xml"
                        }
                    }
                    """, "src/psalm.xml");
  }

  @Test
  public void testNoScriptsSection() {
    doRulesetTest("""
                    
                    {
                    }
                    """, null);
  }

  @Test
  public void testNoPsalmInScriptsSection() {
    doRulesetTest("""
                    
                    {
                        "scripts": {
                        }
                    }
                    """, null);
  }

  @Test
  public void testConfigFormat() {
    doRulesetTest("""
                    
                    {
                      "scripts": {
                            "psalm": "./vendor/bin/psalm --config=src/psalm.xml"
                      }
                    }
                    """, "src/psalm.xml");
  }

  @Test
  public void testNoConfigArg() {
    doRulesetTest("""
                    
                    {
                      "scripts": {
                            "psalm": "@php ./vendor/bin/psalm --init"
                      }
                    }
                    """, null);
  }

  @Test
  public void testWrongFormat() {
    doRulesetTest("""
                    
                    {
                    "scripts": {
                            "psalm": "./vendor/bin/psalm -c"
                      }
                    }
                    """, null);
  }
}
