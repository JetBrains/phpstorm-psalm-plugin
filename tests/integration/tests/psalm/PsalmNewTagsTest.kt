package integration.tests.psalm

import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.testFramework.common.publishArtifact
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.php.PhpIndex
import com.jetbrains.php.fixtures.PhpCodeInsightTestFixture.Companion.phpCodeInsightFixture
import com.jetbrains.php.fixtures.PhpTestDataLayout
import com.jetbrains.php.fixtures.phpProjectFixture
import com.jetbrains.php.lang.psi.elements.impl.ArrayCreationExpressionImpl
import com.jetbrains.php.psalm.types.PsalmTypeInferenceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.net.URL
import java.nio.file.Path
import java.util.zip.ZipInputStream
import kotlin.io.path.createDirectories
import kotlin.io.path.div
import kotlin.io.path.exists
import kotlin.io.path.outputStream
import kotlin.io.path.readLines
import kotlin.io.path.writeText

@TestApplication
class PsalmNewTagsTest {
  companion object {
    private val projectFixture = phpProjectFixture()
  }

  private val php by phpCodeInsightFixture(projectFixture, PhpTestDataLayout("newTags").withHome(PsalmTypeInferenceTest.TEST_DATA_HOME))

  @TempDir
  private lateinit var psalmFolder: Path

  @BeforeEach
  fun setUp() {
    val zipUrl = URL("https://github.com/vimeo/psalm/archive/refs/heads/master.zip")
    downloadAndUnpackZip(zipUrl, psalmFolder)
  }

  @Test
  fun testNewTags() {
    val relativePath = "psalm-master/src/Psalm/DocComment.php"
    php.fixture.copyFileToProject((psalmFolder / relativePath).toString(), relativePath)
    val annotations = mutableListOf<String>()
    runReadActionBlocking {
      val classWithAnnotations = PhpIndex.getInstance(php.project).getClassesByFQN("Psalm\\DocComment").first()
      val annotationConstant = classWithAnnotations.fields.first { it.isConstant && it.name == "PSALM_ANNOTATIONS" }
      (annotationConstant.defaultValue as ArrayCreationExpressionImpl).values().forEach {
        annotations.add(it.text)
      }
    }
    val resultFileName = "psalm-tags.txt"
    publishArtifact(resultFileName) { it.writeText(annotations.sorted().joinToString("\n")) }
    val pathToPreviousResults = Path.of(php.testDataPath, "previousResults", resultFileName)
    if(pathToPreviousResults.exists()){
      assertThat(annotations).containsExactlyInAnyOrderElementsOf(pathToPreviousResults.readLines())
    } else {
      println("Previous results are not found at ${pathToPreviousResults.toAbsolutePath()}")
    }
  }

  private fun downloadAndUnpackZip(zipUrl: URL, destinationDirectory: Path) {
    val zipConnection = zipUrl.openConnection()
    val zipInputStream = ZipInputStream(BufferedInputStream(zipConnection.getInputStream()))
    var zipEntry = zipInputStream.nextEntry
    while (zipEntry != null) {
      val file = destinationDirectory / zipEntry.name
      if (zipEntry.isDirectory) {
        file.createDirectories()
      }
      else {
        val fileOutputStream = file.outputStream()
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