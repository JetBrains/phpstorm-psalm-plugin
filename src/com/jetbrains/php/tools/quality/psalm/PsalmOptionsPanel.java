package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.components.JBCheckBox;
import com.jetbrains.php.config.interpreters.PhpTextFieldWithSdkBasedBrowse;
import com.jetbrains.php.tools.quality.QualityToolConfigurationComboBox;
import com.jetbrains.php.tools.quality.QualityToolsOptionsPanel;

import javax.swing.*;

public class PsalmOptionsPanel extends QualityToolsOptionsPanel {
  private JPanel myOptionsPanel;
  private PhpTextFieldWithSdkBasedBrowse myConfigPathTextField;
  private JBCheckBox myShowInfoJBCheckBox;
  private JBCheckBox myFindUnusedCheckbox;
  private JBCheckBox myFindUnusedSuppressCheckbox;
  private final Project myProject;


  public PsalmOptionsPanel(Project project, QualityToolConfigurationComboBox comboBox) {
    myProject = project;
    PsalmProjectConfiguration configuration = PsalmProjectConfiguration.getInstance(project);
    myConfigPathTextField.setText(configuration.getConfig());
    myConfigPathTextField
        .init(project, getSdkAdditionalData(project, comboBox), PsalmBundle.message("psalm.configuration.file"), true, false);
    myShowInfoJBCheckBox.setSelected(configuration.isShowInfo());
    myFindUnusedCheckbox.setSelected(configuration.isFindUnusedCode());
    myFindUnusedSuppressCheckbox.setSelected(configuration.isFindUnusedSuppress());
  }
  
  private void createUIComponents(){
  }

  @Override
  public JPanel getOptionsPanel() {
    return myOptionsPanel;
  }

  @Override
  public void reset() {
    PsalmProjectConfiguration configuration = PsalmProjectConfiguration.getInstance(myProject);
    myConfigPathTextField.setText(configuration.getConfig());
    myShowInfoJBCheckBox.setSelected(configuration.isShowInfo());
    myFindUnusedCheckbox.setSelected(configuration.isFindUnusedCode());
    myFindUnusedSuppressCheckbox.setSelected(configuration.isFindUnusedSuppress());
  }

  @Override
  public boolean isModified() {
    PsalmProjectConfiguration configuration = PsalmProjectConfiguration.getInstance(myProject);
    if (!StringUtil.equals(myConfigPathTextField.getText(), configuration.getConfig())) return true;
    if (myShowInfoJBCheckBox.isSelected() != configuration.isShowInfo()) return true;
    if (myFindUnusedCheckbox.isSelected() != configuration.isFindUnusedCode()) return true;
    if (myFindUnusedSuppressCheckbox.isSelected() != configuration.isFindUnusedSuppress()) return true;
    return false;
  }

  @Override
  public void apply() {
    PsalmProjectConfiguration configuration = PsalmProjectConfiguration.getInstance(myProject);
    configuration.setConfig(myConfigPathTextField.getText());
    configuration.setFindUnusedCode(myFindUnusedCheckbox.isSelected());
    configuration.setFindUnusedSuppress(myFindUnusedSuppressCheckbox.isSelected());
    configuration.setShowInfo(myShowInfoJBCheckBox.isSelected());
  }
}
