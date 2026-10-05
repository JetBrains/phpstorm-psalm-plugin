package com.jetbrains.php.tools.quality.psalm

import com.intellij.openapi.diagnostic.logger
import com.jetbrains.php.tools.quality.ParsedProblem
import com.jetbrains.php.tools.quality.QualityToolMessage.Severity
import com.jetbrains.php.tools.quality.ReportParser
import org.xml.sax.Attributes
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import java.io.StringReader
import javax.xml.parsers.SAXParserFactory

private val LOG = logger<PsalmReportParser>()

/**
 * Parses the Psalm XML report.
 * Every `error` and `warning` element is one problem.
 */
class PsalmReportParser : ReportParser {

  override fun parse(report: CharSequence): List<ParsedProblem> {
    val handler = Handler()
    try {
      SAXParserFactory.newDefaultInstance().newSAXParser().parse(InputSource(StringReader(report.toString())), handler)
    }
    catch (e: SAXException) {
      LOG.debug("Malformed Psalm report", e)
    }
    return handler.problems
  }

  private class Handler : DefaultHandler() {
    val problems = ArrayList<ParsedProblem>()
    private var file: String? = null

    override fun startElement(uri: String?, localName: String?, qName: String, attributes: Attributes) {
      when (qName) {
        FILE_TAG -> file = attributes.getValue(FILE_NAME_ATTR)
        ERROR_TAG, WARNING_TAG -> addProblem(attributes)
      }
    }

    private fun addProblem(attributes: Attributes) {
      val line = attributes.getValue(LINE_NUMBER_ATTR)?.toIntOrNull()
      val message = attributes.getValue(MESSAGE_ATTR)
      if (line == null || line < 0 || message == null) {
        LOG.debug("Skipped a Psalm problem without a valid line or message")
        return
      }
      val column = attributes.getValue(COLUMN_NUMBER_ATTR)?.toIntOrNull() ?: 0
      val severity = if (attributes.getValue(SEVERITY_ATTR) == ERROR_TAG) Severity.ERROR else Severity.WARNING
      problems.add(ParsedProblem(severity, line, maxOf(0, column - 1), message, file))
    }
  }

  private companion object {
    const val FILE_TAG = "file"
    const val ERROR_TAG = "error"
    const val WARNING_TAG = "warning"
    const val FILE_NAME_ATTR = "name"
    const val LINE_NUMBER_ATTR = "line"
    const val COLUMN_NUMBER_ATTR = "column"
    const val MESSAGE_ATTR = "message"
    const val SEVERITY_ATTR = "severity"
  }
}
