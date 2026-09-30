package com.jetbrains.php.tools.quality.psalm

import com.intellij.openapi.util.io.FileUtil
import com.intellij.profile.codeInspection.InspectionProjectProfileManager
import com.intellij.psi.PsiFile
import com.jetbrains.php.fixtures.PhpHeavyCodeInsightFixtureTestCase
import com.jetbrains.php.tools.quality.QualityToolAnnotator
import com.jetbrains.php.tools.quality.QualityToolAnnotatorInfo
import com.jetbrains.php.tools.quality.QualityToolMessage
import java.nio.file.Files
import java.nio.file.Path

class PsalmTempFileMessageTest : PhpHeavyCodeInsightFixtureTestCase() {
  private val tempRoot: Path by lazy { Files.createTempDirectory("Psalmtemp_folder") }

  override fun tearDown() {
    try {
      FileUtil.delete(tempRoot.toFile())
    }
    finally {
      super.tearDown()
    }
  }

  fun testMissingFileThatExistsInProjectIsDropped() {
    val messages = run(tempFileSet = true, target = "vendor/autoload.php", createTarget = true)
    assertEmpty(messages)
  }

  fun testMissingFileThatIsReallyMissingShowsOriginalPath() {
    val messages = run(tempFileSet = true, target = "vendor/autoload.php", createTarget = false)
    val text = messages.single().messageText
    assertTrue(text, text.contains(originalRoot().resolve("vendor/autoload.php").toString().replace('\\', '/')))
    assertFalse(text, text.contains("Psalmtemp_folder"))
  }

  fun testMissingFileIsKeptWithoutTempFile() {
    val messages = run(tempFileSet = false, target = "vendor/autoload.php", createTarget = true)
    assertTrue(messages.single().messageText, messages.single().messageText.contains("Psalmtemp_folder"))
  }

  fun testOtherMessageIsKept() {
    val messages = run(tempFileSet = true, message = "UnusedVariable: Variable \$fff is never referenced")
    assertEquals("psalm: UnusedVariable: Variable \$fff is never referenced", messages.single().messageText)
  }

  fun testMissingFileOutsideTempFolderIsKept() {
    val message = "MissingFile: Cannot find file /somewhere/else/vendor/autoload.php to include"
    val messages = run(tempFileSet = true, message = message)
    assertEquals("psalm: $message", messages.single().messageText)
  }

  private val originalFile: PsiFile by lazy {
    myFixture.addFileToProject("tests/bootstrap.php", "<?php\nrequire dirname(__DIR__) . '/vendor/autoload.php';\n")
  }

  /**
   * The directory that the temp folder stands for: the project base for a file in the project, or the directory of the file otherwise.
   */
  private fun originalRoot(): Path {
    val file = originalFile.virtualFile
    val suffix = "/" + QualityToolAnnotator.getPathInTempFolder(project, file)
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
    myFixture.enableInspections(PsalmGlobalInspection())
    val profile = InspectionProjectProfileManager.getInstance(project).currentProfile
    val info = QualityToolAnnotatorInfo(originalFile, PsalmValidationInspection(), profile, project, null, "psalm", 100, 1000, true)
    if (tempFileSet) {
      info.tempFile = tempRoot.resolve(QualityToolAnnotator.getPathInTempFolder(project, originalFile.virtualFile)).toString()
    }

    val processor = PsalmMessageProcessor(info)
    processor.parseLine("<file name=\"bootstrap.php\">\n")
    processor.parseLine(" <error line=\"2\" column=\"1\" severity=\"error\" message=\"$message\"/>\n")
    processor.parseLine("</file>\n")
    processor.done()
    return processor.messages
  }

  override fun getFixtureTestDataFolder(): String = "output"

  override fun getBasePath(): String = "/phpstorm/psalm/testData/output"
}
