package com.jetbrains.php.tools.quality.psalm

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.util.ui.UIUtil
import com.jetbrains.php.config.interpreters.PhpTextFieldWithSdkBasedBrowse
import com.jetbrains.php.tools.quality.QualityToolConfigurationComboBox
import com.jetbrains.php.tools.quality.ui.QualityToolRateLimitPanel
import javax.swing.JCheckBox
import javax.swing.JLabel

class PsalmOptionsPanelParityTest : BasePlatformTestCase() {

  private val captions = listOf("Show info", "Find unused code", "Find unused @psalm-suppress")

  private fun comboBox() = QualityToolConfigurationComboBox(project, PsalmQualityToolType.INSTANCE)

  private fun createPanel(): PsalmOptionsPanel = PsalmOptionsPanel(project, comboBox(), Runnable {})

  private fun config(): PsalmOptionsConfiguration = PsalmOptionsConfiguration.getInstance(project)

  fun testResetIsNotModifiedThenDetectsChange() {
    config().apply {
      setShowInfo(true)
      setFindUnusedCode(false)
      setFindUnusedSuppress(true)
      setConfig("/tmp/psalm.xml")
    }
    val panel = createPanel()
    panel.reset()
    assertFalse("Freshly reset panel must not be modified", panel.isModified)

    config().setShowInfo(false)
    assertTrue("isModified must detect the checkbox differing from the configuration", panel.isModified)
  }

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

  fun testStructurePlacesControls() {
    val root = createPanel().getOptionsPanel()

    val checkboxTexts = UIUtil.findComponentsOfType(root, JCheckBox::class.java)
      .mapNotNull { it.text?.filterNot(Char::isISOControl) }
    for (caption in captions) {
      assertTrue("Missing checkbox '$caption' in $checkboxTexts", checkboxTexts.any { it.contains(caption) })
    }

    assertNotNull("Config-path field missing",
                  UIUtil.findComponentOfType(root, PhpTextFieldWithSdkBasedBrowse::class.java))
    assertNotNull("Rate-limit sub-panel missing",
                  UIUtil.findComponentOfType(root, QualityToolRateLimitPanel::class.java))

    val labels = UIUtil.findComponentsOfType(root, JLabel::class.java).mapNotNull { it.text?.filterNot(Char::isISOControl) }
    assertTrue("Configuration-file label missing: $labels", labels.any { it.contains("Configuration file") })
  }
}
