package com.jetbrains.php.psalm.quality.tools

import com.intellij.testFramework.fixtures.CodeInsightFixtureTestCase
import com.jetbrains.php.tools.quality.psalm.PsalmGlobalInspection

class PsalmCommandLineTest : CodeInsightFixtureTestCase<Nothing?>() {
  fun testBaseOptionsCarryNoOutputFlags() {
    val options = PsalmGlobalInspection.getBaseCommandLineOptions("/tmp/file.php", project)
    assertTrue(options.contains("/tmp/file.php"))
    assertFalse(options.any { it.startsWith("--output-format") })
    assertFalse(options.contains("--monochrome"))
  }
}
