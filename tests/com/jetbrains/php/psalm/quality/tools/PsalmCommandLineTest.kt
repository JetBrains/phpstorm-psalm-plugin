package com.jetbrains.php.psalm.quality.tools

import com.intellij.platform.testFramework.junit5.codeInsight.fixture.codeInsightProjectFixture
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.php.tools.quality.psalm.PsalmGlobalInspection
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@TestApplication
class PsalmCommandLineTest {
  private val projectFixture = codeInsightProjectFixture()
  private val project get() = projectFixture.get()

  @Test
  fun testBaseOptionsCarryNoOutputFlags() {
    val options = PsalmGlobalInspection.getBaseCommandLineOptions("/tmp/file.php", project)
    assertTrue(options.contains("/tmp/file.php"))
    assertFalse(options.any { it.startsWith("--output-format") })
    assertFalse(options.contains("--monochrome"))
  }
}
