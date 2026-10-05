package com.jetbrains.php.psalm.quality.tools

import com.jetbrains.php.tools.quality.ParsedProblem
import com.jetbrains.php.tools.quality.QualityToolMessage.Severity
import com.jetbrains.php.tools.quality.psalm.PsalmReportParser
import junit.framework.TestCase

class PsalmReportParserTest : TestCase() {
  private val report = """
    <?xml version="1.0" encoding="UTF-8"?>
    <checkstyle>
      <file name="src/a.php">
        <error line="3" column="5" severity="error" message="Unused variable ${'$'}x"/>
        <warning line="7" severity="info" message="Dead code"/>
      </file>
      <file name="src/b.php">
        <error line="1" column="1" severity="error" message="Other file"/>
      </file>
    </checkstyle>
  """.trimIndent()

  fun testProblemsOfAllFiles() {
    assertEquals(
      listOf(
        ParsedProblem(Severity.ERROR, 3, 4, "Unused variable \$x", "src/a.php"),
        ParsedProblem(Severity.WARNING, 7, 0, "Dead code", "src/a.php"),
        ParsedProblem(Severity.ERROR, 1, 0, "Other file", "src/b.php"),
      ),
      PsalmReportParser().parse(report),
    )
  }

  fun testEmptyReport() {
    assertTrue(PsalmReportParser().parse("""<checkstyle></checkstyle>""").isEmpty())
  }

  fun testProblemWithoutLineIsSkipped() {
    val problems = PsalmReportParser().parse("""<file name="a.php"><error severity="error" message="m"/></file>""")
    assertTrue(problems.isEmpty())
  }

  fun testProblemWithBadLineIsSkipped() {
    val problems = PsalmReportParser().parse("""<file name="a.php"><error line="x" severity="error" message="m"/></file>""")
    assertTrue(problems.isEmpty())
  }

  fun testMissingColumnGivesZero() {
    val problems = PsalmReportParser().parse("""<file name="a.php"><error line="2" severity="error" message="m"/></file>""")
    assertEquals(0, problems.single().column)
  }

  fun testOtherElementIsNotAProblem() {
    val problems = PsalmReportParser().parse("""<file name="a.php"><note line="2" severity="error" message="m"/></file>""")
    assertTrue(problems.isEmpty())
  }

  fun testMalformedReportKeepsEarlierProblems() {
    val problems = PsalmReportParser().parse(report.substringBefore("</file>") + "<broken")
    assertEquals(2, problems.size)
  }
}
