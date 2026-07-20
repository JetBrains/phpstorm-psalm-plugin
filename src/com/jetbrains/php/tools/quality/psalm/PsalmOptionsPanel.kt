package com.jetbrains.php.tools.quality.psalm

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.ui.DocumentAdapter
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.panel
import com.jetbrains.php.config.interpreters.PhpTextFieldWithSdkBasedBrowse
import com.jetbrains.php.tools.quality.QualityToolConfigurationComboBox
import com.jetbrains.php.tools.quality.QualityToolsOptionsPanel
import com.jetbrains.php.tools.quality.ui.QualityToolRateLimitPanel
import com.jetbrains.php.tools.quality.ui.QualityToolRateLimitUI
import javax.swing.JPanel
import javax.swing.SwingConstants
import javax.swing.event.DocumentEvent

class PsalmOptionsPanel(
  project: Project,
  private val myComboBox: QualityToolConfigurationComboBox<*>,
  validate: Runnable,
) : QualityToolsOptionsPanel(project, validate, PsalmQualityToolType.INSTANCE) {

  private val myConfigPathTextField = PhpTextFieldWithSdkBasedBrowse()
  private val myRateLimitPanel = QualityToolRateLimitPanel()
  private lateinit var myShowInfoJBCheckBox: JBCheckBox
  private lateinit var myFindUnusedCheckbox: JBCheckBox
  private lateinit var myFindUnusedSuppressCheckbox: JBCheckBox

  private val myOptionsPanel = panel {
    row(PsalmBundle.message("psalm.configuration.file.")) {
      cell(myConfigPathTextField).align(AlignX.FILL)
    }
    row {
      myShowInfoJBCheckBox = checkBox(PsalmBundle.message("psalm.show.info")).component
    }
    row {
      myFindUnusedCheckbox = checkBox(PsalmBundle.message("psalm.find.unused"))
        .applyToComponent { horizontalAlignment = SwingConstants.LEFT }
        .component
    }
    row {
      myFindUnusedSuppressCheckbox = checkBox(PsalmBundle.message("psalm.find.unused.supress")).component
    }
    row {
      cell(myRateLimitPanel).align(AlignX.FILL)
    }
  }

  init {
    val configuration = PsalmOptionsConfiguration.getInstance(project)
    myRateLimitPanel.configure(QualityToolRateLimitUI.DEFAULT_UI)
    myRateLimitPanel.reset(configuration.rateLimitSettings)
    myConfigPathTextField.text = configuration.config
    myConfigPathTextField.init(project, getSdkAdditionalData(project, myComboBox),
                               PsalmBundle.message("psalm.configuration.file"), true, false)
    myShowInfoJBCheckBox.isSelected = configuration.isShowInfo
    myFindUnusedCheckbox.isSelected = configuration.isFindUnusedCode
    myFindUnusedSuppressCheckbox.isSelected = configuration.isFindUnusedSuppress
    myConfigPathTextField.textField.document.addDocumentListener(object : DocumentAdapter() {
      override fun textChanged(e: DocumentEvent) {
        validate.run()
      }
    })
  }

  override fun validatePath(): String? {
    val interpreter = getSelectedInterpreter(myProject, myComboBox)
    if (interpreter != null && interpreter.isRemote) {
      //TODO: validate remote path?
      return null
    }
    val url = VfsUtilCore.convertToURL(VfsUtilCore.pathToUrl(myConfigPathTextField.text))
    if (url == null || VfsUtil.findFileByURL(url) == null) {
      return PsalmBundle.message("config.file.doesnt.exist")
    }
    return null
  }

  override fun getOptionsPanel(): JPanel = myOptionsPanel

  override fun reset() {
    val configuration = PsalmOptionsConfiguration.getInstance(myProject)
    myRateLimitPanel.reset(configuration.rateLimitSettings)
    myConfigPathTextField.text = configuration.config
    myShowInfoJBCheckBox.isSelected = configuration.isShowInfo
    myFindUnusedCheckbox.isSelected = configuration.isFindUnusedCode
    myFindUnusedSuppressCheckbox.isSelected = configuration.isFindUnusedSuppress
  }

  override fun isModified(): Boolean {
    val configuration = PsalmOptionsConfiguration.getInstance(myProject)
    if (myRateLimitPanel.isModified(configuration.rateLimitSettings)) return true
    if (!StringUtil.equals(myConfigPathTextField.text, configuration.config)) return true
    if (myShowInfoJBCheckBox.isSelected != configuration.isShowInfo) return true
    if (myFindUnusedCheckbox.isSelected != configuration.isFindUnusedCode) return true
    if (myFindUnusedSuppressCheckbox.isSelected != configuration.isFindUnusedSuppress) return true
    return false
  }

  override fun apply() {
    val configuration = PsalmOptionsConfiguration.getInstance(myProject)
    myRateLimitPanel.applyTo(configuration.rateLimitSettings)
    configuration.config = myConfigPathTextField.text
    configuration.isFindUnusedCode = myFindUnusedCheckbox.isSelected
    configuration.isFindUnusedSuppress = myFindUnusedSuppressCheckbox.isSelected
    configuration.isShowInfo = myShowInfoJBCheckBox.isSelected
  }
}
