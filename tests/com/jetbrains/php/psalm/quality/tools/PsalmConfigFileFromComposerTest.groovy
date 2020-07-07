package com.jetbrains.php.psalm.quality.tools

import com.intellij.testFramework.fixtures.CodeInsightFixtureTestCase
import com.jetbrains.php.tools.quality.psalm.PsalmComposerConfig
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable 

class PsalmConfigFileFromComposerTest extends CodeInsightFixtureTestCase {

  @NotNull
  private void doRulesetTest(@NotNull String content, @Nullable String expectedPath) {
    myFixture.configureByText("composer.json", content)
    try {
      def configFile = myFixture.getFile().getVirtualFile()
      assertEquals(expectedPath, new PsalmComposerConfig().getRuleset(configFile))
    }
    finally {
    }
  }

  void testCFormat() {
    doRulesetTest('''
{
    "scripts": {
        "psalm": "./vendor/bin/psalm -c src/psalm.xml"
    }
}
''', "src/psalm.xml")
  }

  void testNoScriptsSection() {
    doRulesetTest('''
{
}
''', null)
  }

  void testNoPsalmInScriptsSection() {
    doRulesetTest('''
{
    "scripts": {
    }
}
''', null)
  }
  

  void testConfigFormat() {
    doRulesetTest('''
{
  "scripts": {
        "psalm": "./vendor/bin/psalm --config=src/psalm.xml"
  }
}
''', "src/psalm.xml")
  }

  void testNoConfigArg() {
    doRulesetTest('''
{
  "scripts": {
        "psalm": "@php ./vendor/bin/psalm --init"
  }
}
''', null)
  }


  void testWrongFormat() {
    doRulesetTest('''
{
"scripts": {
        "psalm": "./vendor/bin/psalm -c"
  }
}
''', null)
  }

}