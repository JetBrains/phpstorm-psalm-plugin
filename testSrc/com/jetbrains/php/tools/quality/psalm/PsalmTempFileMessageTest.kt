package com.jetbrains.php.tools.quality.psalm

import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.openapi.util.io.FileUtil
import com.intellij.profile.codeInspection.InspectionProjectProfileManager
import com.intellij.psi.PsiFile
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.php.fixtures.PhpHeavyCodeInsightTestFixture.Companion.phpHeavyCodeInsightFixture
import com.jetbrains.php.fixtures.PhpTestDataLayout
import com.jetbrains.php.tools.quality.QualityToolAnnotator
import com.jetbrains.php.tools.quality.QualityToolAnnotatorInfo
import com.jetbrains.php.tools.quality.QualityToolMessage
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path

@TestApplication
class PsalmTempFileMessageTest {
  private val php by phpHeavyCodeInsightFixture(PhpTestDataLayout("output").withHome("/phpstorm/psalm/testData/").withoutTestDir())

  private val tempRoot: Path by lazy { Files.createTempDirectory("Psalmtemp_folder") }

  @AfterEach
  fun tearDown() {
    FileUtil.delete(tempRoot.toFile())
  }

  @Test
  fun testMissingFileThatExistsInProjectIsDropped() {
    val messages = run(tempFileSet = true, target = "vendor/autoload.php", createTarget = true)
    assertTrue(messages.isEmpty(), messages.toString())
  }

  @Test
  fun testMissingFileThatIsReallyMissingShowsOriginalPath() {
    val messages = run(tempFileSet = true, target = "vendor/autoload.php", createTarget = false)
    val text = messages.single().messageText
    assertTrue(text.contains(originalRoot().resolve("vendor/autoload.php").toString().replace('\\', '/')), text)
    assertFalse(text.contains("Psalmtemp_folder"), text)
  }

  @Test
  fun testMissingFileIsKeptWithoutTempFile() {
    val messages = run(tempFileSet = false, target = "vendor/autoload.php", createTarget = true)
    assertTrue(messages.single().messageText.contains("Psalmtemp_folder"), messages.single().messageText)
  }

  @Test
  fun testOtherMessageIsKept() {
    val messages = run(tempFileSet = true, message = "UnusedVariable: Variable \$fff is never referenced")
    assertEquals("psalm: UnusedVariable: Variable \$fff is never referenced", messages.single().messageText)
  }

  @Test
  fun testMissingFileOutsideTempFolderIsKept() {
    val message = "MissingFile: Cannot find file /somewhere/else/vendor/autoload.php to include"
    val messages = run(tempFileSet = true, message = message)
    assertEquals("psalm: $message", messages.single().messageText)
  }

  private val originalFile: PsiFile by lazy {
    php.fixture.addFileToProject("tests/bootstrap.php", "<?php\nrequire dirname(__DIR__) . '/vendor/autoload.php';\n")
  }

  /**
   * The directory that the temp folder stands for: the project base for a file in the project, or the directory of the file otherwise.
   */
  private fun originalRoot(): Path {
    val file = originalFile.virtualFile
    val suffix = "/" + QualityToolAnnotator.getPathInTempFolder(php.project, file)
    return Path.of(file.path.removeSuffix(suffix))
  }

  private fun run(
    tempFileSet: Boolean,
    target: String = "",
    createTarget: Boolean = false,
    message: String = "MissingFile: Cannot find file ${tempRoot.resolve(target).toString().replace('\\', '/')} to include",
  ): List<QualityToolMessage> {
    if (createTarget) {
      val path = originalRoot().resolve(target)
      Files.createDirectories(path.parent)
      Files.writeString(path, "<?php\n")
    }
    php.fixture.enableInspections(PsalmGlobalInspection())
    val profile = InspectionProjectProfileManager.getInstance(php.project).currentProfile
    val file = originalFile
    val info = runReadActionBlocking {
      QualityToolAnnotatorInfo(file, PsalmValidationInspection(), profile, php.project, null, "psalm", 100, 1000, true)
    }
    if (tempFileSet) {
      info.tempFile = tempRoot.resolve(QualityToolAnnotator.getPathInTempFolder(php.project, originalFile.virtualFile)).toString()
    }

    val processor = PsalmMessageProcessor(info)
    processor.parseLine("<file name=\"bootstrap.php\">\n")
    processor.parseLine(" <error line=\"2\" column=\"1\" severity=\"error\" message=\"$message\"/>\n")
    processor.parseLine("</file>\n")
    processor.done()
    return processor.messages
  }
}
