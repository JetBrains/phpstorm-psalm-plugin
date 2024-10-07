package com.jetbrains.php.psalm.quality.tools;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.fixtures.CodeInsightFixtureTestCase;
import com.jetbrains.php.tools.quality.psalm.PsalmComposerConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsalmConfigFileFromComposerTest extends CodeInsightFixtureTestCase {
  private void doRulesetTest(@NotNull String content, @Nullable String expectedPath) {
    myFixture.configureByText("composer.json", content);
    VirtualFile configFile = myFixture.getFile().getVirtualFile();
    assertEquals(expectedPath, new PsalmComposerConfig().getRuleset(configFile));
  }

  public void testCFormat() {
    doRulesetTest("""
                    
                    {
                        "scripts": {
                            "psalm": "./vendor/bin/psalm -c src/psalm.xml"
                        }
                    }
                    """, "src/psalm.xml");
  }

  public void testNoScriptsSection() {
    doRulesetTest("""
                    
                    {
                    }
                    """, null);
  }

  public void testNoPsalmInScriptsSection() {
    doRulesetTest("""
                    
                    {
                        "scripts": {
                        }
                    }
                    """, null);
  }

  public void testConfigFormat() {
    doRulesetTest("""
                    
                    {
                      "scripts": {
                            "psalm": "./vendor/bin/psalm --config=src/psalm.xml"
                      }
                    }
                    """, "src/psalm.xml");
  }

  public void testNoConfigArg() {
    doRulesetTest("""
                    
                    {
                      "scripts": {
                            "psalm": "@php ./vendor/bin/psalm --init"
                      }
                    }
                    """, null);
  }

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
