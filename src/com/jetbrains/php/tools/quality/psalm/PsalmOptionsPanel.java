package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.components.JBCheckBox;
import com.jetbrains.php.config.interpreters.PhpInterpreter;
import com.jetbrains.php.config.interpreters.PhpTextFieldWithSdkBasedBrowse;
import com.jetbrains.php.tools.quality.QualityToolConfigurationComboBox;
import com.jetbrains.php.tools.quality.QualityToolsOptionsPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.net.URL;

import static com.intellij.openapi.vfs.VfsUtil.findFileByURL;
import static com.intellij.openapi.vfs.VfsUtilCore.convertToURL;
import static com.intellij.openapi.vfs.VfsUtilCore.pathToUrl;

public class PsalmOptionsPanel extends QualityToolsOptionsPanel {
  private JPanel myOptionsPanel;
  private PhpTextFieldWithSdkBasedBrowse myConfigPathTextField;
  private JBCheckBox myShowInfoJBCheckBox;
  private JBCheckBox myFindUnusedCheckbox;
  private JBCheckBox myFindUnusedSuppressCheckbox;
  private final QualityToolConfigurationComboBox myComboBox;

  public PsalmOptionsPanel(Project project, QualityToolConfigurationComboBox comboBox, Runnable validate) {
    super(project, validate, PsalmQualityToolType.INSTANCE);
    myComboBox = comboBox;
    PsalmOptionsConfiguration configuration = PsalmOptionsConfiguration.getInstance(project);
    myConfigPathTextField.setText(configuration.getConfig());
    myConfigPathTextField
      .init(project, getSdkAdditionalData(project, comboBox), PsalmBundle.message("psalm.configuration.file"), true, false);
    myShowInfoJBCheckBox.setSelected(configuration.isShowInfo());
    myFindUnusedCheckbox.setSelected(configuration.isFindUnusedCode());
    myFindUnusedSuppressCheckbox.setSelected(configuration.isFindUnusedSuppress());
    myConfigPathTextField.getTextField().getDocument().addDocumentListener(new DocumentAdapter() {
      @Override
      protected void textChanged(@NotNull DocumentEvent e) {
        validate.run();
      }
    });
  }

  private void createUIComponents() {
  }

  @Override
  protected @Nullable String validatePath() {
    PhpInterpreter interpreter = getSelectedInterpreter(myProject, myComboBox);
    if (interpreter != null && interpreter.isRemote()) {
      //TODO: validate remote path?
      return null;
    }
    final URL url = convertToURL(pathToUrl(myConfigPathTextField.getText()));
    if (url == null || findFileByURL(url) == null) {
      return PsalmBundle.message("config.file.doesnt.exist");
    }
    return null;
  }

  @Override
  public JPanel getOptionsPanel() {
    return myOptionsPanel;
  }

  @Override
  public void reset() {
    PsalmOptionsConfiguration configuration = PsalmOptionsConfiguration.getInstance(myProject);
    myConfigPathTextField.setText(configuration.getConfig());
    myShowInfoJBCheckBox.setSelected(configuration.isShowInfo());
    myFindUnusedCheckbox.setSelected(configuration.isFindUnusedCode());
    myFindUnusedSuppressCheckbox.setSelected(configuration.isFindUnusedSuppress());
  }

  @Override
  public boolean isModified() {
    PsalmOptionsConfiguration configuration = PsalmOptionsConfiguration.getInstance(myProject);
    if (!StringUtil.equals(myConfigPathTextField.getText(), configuration.getConfig())) return true;
    if (myShowInfoJBCheckBox.isSelected() != configuration.isShowInfo()) return true;
    if (myFindUnusedCheckbox.isSelected() != configuration.isFindUnusedCode()) return true;
    if (myFindUnusedSuppressCheckbox.isSelected() != configuration.isFindUnusedSuppress()) return true;
    return false;
  }

  @Override
  public void apply() {
    PsalmOptionsConfiguration configuration = PsalmOptionsConfiguration.getInstance(myProject);
    configuration.setConfig(myConfigPathTextField.getText());
    configuration.setFindUnusedCode(myFindUnusedCheckbox.isSelected());
    configuration.setFindUnusedSuppress(myFindUnusedSuppressCheckbox.isSelected());
    configuration.setShowInfo(myShowInfoJBCheckBox.isSelected());
  }
}
