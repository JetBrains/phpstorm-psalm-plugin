package com.jetbrains.php.tools.quality.psalm

import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import com.intellij.platform.testFramework.junit5.codeInsight.fixture.codeInsightProjectFixture
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.testFramework.junit5.TestDisposable
import com.intellij.util.ui.UIUtil
import com.jetbrains.php.config.interpreters.PhpTextFieldWithSdkBasedBrowse
import com.jetbrains.php.tools.quality.QualityToolConfigurationComboBox
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import javax.swing.JCheckBox
import javax.swing.JLabel

@TestApplication
class PsalmOptionsPanelParityTest {
  private val projectFixture = codeInsightProjectFixture()
  private val project get() = projectFixture.get()

  @TestDisposable
  lateinit var testRootDisposable: Disposable

  private val captions = listOf("Show info", "Find unused code", "Find unused @psalm-suppress")

  // the combo box is a Disposable that holds the project; nothing else owns it in the test
  private fun comboBox() = QualityToolConfigurationComboBox(project, PsalmQualityToolType.INSTANCE)
    .also { Disposer.register(testRootDisposable, it) }

  private fun createPanel(): PsalmOptionsPanel = PsalmOptionsPanel(project, comboBox(), Runnable {})

  private fun config(): PsalmOptionsConfiguration = PsalmOptionsConfiguration.getInstance(project)

  @Test
  fun testResetIsNotModifiedThenDetectsChange() {
    config().apply {
      setShowInfo(true)
      setFindUnusedCode(false)
      setFindUnusedSuppress(true)
      setConfig("/tmp/psalm.xml")
    }
    val panel = createPanel()
    panel.reset()
    assertFalse(panel.isModified, "Freshly reset panel must not be modified")

    config().setShowInfo(false)
    assertTrue(panel.isModified, "isModified must detect the checkbox differing from the configuration")
  }

  @Test
  fun testApplyWritesUiStateBack() {
    config().apply {
      setShowInfo(true)
      setFindUnusedCode(true)
      setFindUnusedSuppress(false)
      setConfig("/tmp/psalm.xml")
    }
    val panel = createPanel()
    panel.reset()

    // mutate the configuration underneath; apply() must push the (reset) UI state back over it
    config().apply {
      setShowInfo(false)
      setFindUnusedCode(false)
      setFindUnusedSuppress(true)
      setConfig("/other.xml")
    }
    panel.apply()
    assertTrue(config().isShowInfo)
    assertTrue(config().isFindUnusedCode)
    assertFalse(config().isFindUnusedSuppress)
    assertEquals("/tmp/psalm.xml", config().config)
  }

  @Test
  fun testStructurePlacesControls() {
    val root = createPanel().getOptionsPanel()

    val checkboxTexts = UIUtil.findComponentsOfType(root, JCheckBox::class.java)
      .mapNotNull { it.text?.filterNot(Char::isISOControl) }
    for (caption in captions) {
      assertTrue(checkboxTexts.any { it.contains(caption) }, "Missing checkbox '$caption' in $checkboxTexts")
    }

    assertNotNull(UIUtil.findComponentOfType(root, PhpTextFieldWithSdkBasedBrowse::class.java),
                  "Config-path field missing")

    val labels = UIUtil.findComponentsOfType(root, JLabel::class.java).mapNotNull { it.text?.filterNot(Char::isISOControl) }
    assertTrue(labels.any { it.contains("Configuration file") }, "Configuration-file label missing: $labels")
  }
}
