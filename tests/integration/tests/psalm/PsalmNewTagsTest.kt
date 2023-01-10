package integration.tests.psalm

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.util.io.FileUtil
import com.intellij.testFramework.UsefulTestCase
import com.jetbrains.php.PhpIndex
import com.jetbrains.php.fixtures.PhpCodeInsightFixtureTestCase
import com.jetbrains.php.lang.psi.elements.impl.ArrayCreationExpressionImpl
import com.jetbrains.php.psalm.types.PsalmTypeInferenceTest
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.zip.ZipInputStream
import kotlin.io.path.div

class PsalmNewTagsTest : PhpCodeInsightFixtureTestCase() {

  private lateinit var psalmFolder: File

  override fun getFixtureTestDataFolder(): String {
    return "newTags"
  }

  override fun getTestDataHome(): String {
    return PsalmTypeInferenceTest.TEST_DATA_HOME
  }


  override fun setUp() {
    super.setUp()
    psalmFolder = File(PathManager.getHomePath() + "/" + basePath)
    FileUtil.createDirectory(psalmFolder)
    val zipUrl = URL("https://github.com/vimeo/psalm/archive/refs/heads/master.zip")
    downloadAndUnpackZip(zipUrl, psalmFolder)
  }

  override fun tearDown() {
    try {
      FileUtil.deleteRecursively(psalmFolder.toPath())
    }
    catch (e: Throwable) {
      addSuppressedException(e)
    }
    finally {
      super.tearDown()
    }
  }

  fun testNewTags() {
    myFixture.copyFileToProject("psalm-master/src/Psalm/DocComment.php")
    val classWithAnnotations = PhpIndex.getInstance(project).getClassesByFQN("Psalm\\DocComment").first()
    val annotationConstant = classWithAnnotations.fields.first { it.isConstant && it.name == "PSALM_ANNOTATIONS" }
    val annotations = mutableListOf<String>()
    (annotationConstant.defaultValue as ArrayCreationExpressionImpl).values().forEach {
      annotations.add(it.text)
    }
    val resultFileName = "psalm-tags.txt"
    val resultFile = FileUtil.createTempFile(resultFileName, "")
    resultFile.writeText(annotations.joinToString("\n"))
    println("##teamcity[publishArtifacts '${resultFile.absolutePath}']")
    val pathToPreviousResults = (psalmFolder.toPath() / "previousResults" / resultFileName).toFile()
    if(pathToPreviousResults.exists()){
      UsefulTestCase.assertSameElements(annotations, pathToPreviousResults.readLines())
    } else {
      println("Previous results are not found at ${pathToPreviousResults.absolutePath}")
    }
  }

  private fun downloadAndUnpackZip(zipUrl: URL, destinationDirectory: File) {
    val zipConnection = zipUrl.openConnection()
    val zipInputStream = ZipInputStream(BufferedInputStream(zipConnection.getInputStream()))
    var zipEntry = zipInputStream.nextEntry
    while (zipEntry != null) {
      val file = File(destinationDirectory, zipEntry.name)
      if (zipEntry.isDirectory) {
        file.mkdirs()
      }
      else {
        val fileOutputStream = FileOutputStream(file)
        val bufferedOutputStream = BufferedOutputStream(fileOutputStream, 2048)
        val data = ByteArray(2048)
        var count: Int
        while (zipInputStream.read(data, 0, data.size).also { count = it } != -1) {
          bufferedOutputStream.write(data, 0, count)
        }
        bufferedOutputStream.flush()
        bufferedOutputStream.close()
      }
      zipInputStream.closeEntry()
      zipEntry = zipInputStream.nextEntry
    }
    zipInputStream.close()
  }
}