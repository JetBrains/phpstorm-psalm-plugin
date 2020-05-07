package com.jetbrains.php.tools.quality.psalm;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.DocumentAdapter;
import com.jetbrains.php.config.interpreters.PhpInterpreter;
import com.jetbrains.php.config.interpreters.PhpInterpretersManagerImpl;
import com.jetbrains.php.config.interpreters.PhpSdkAdditionalData;
import com.jetbrains.php.config.interpreters.PhpTextFieldWithSdkBasedBrowse;
import com.jetbrains.php.tools.quality.QualityToolValidationException;
import com.jetbrains.php.tools.quality.phpcs.PhpCSConfiguration;
import com.jetbrains.php.tools.quality.phpcs.PhpCSProjectConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;

public class PsalmOptionsPanel {
  private JPanel myOptionsPanel;
  private PhpTextFieldWithSdkBasedBrowse myConfigPathTextField;
  private final PsalmValidationInspection myInspection;

  public PsalmOptionsPanel(PsalmValidationInspection inspection) {
    myInspection = inspection;

    final Project project = getCurrentProject();
    myConfigPathTextField.setText(inspection.config);
    myConfigPathTextField
      .init(project, getSdkAdditionalData(project), PsalmBundle.message("psalm.configuration.file"), true, false);
    myConfigPathTextField.getTextField().getDocument().addDocumentListener(new DocumentAdapter() {
      @Override
      protected void textChanged(@NotNull DocumentEvent e) {
        myInspection.config = myConfigPathTextField.getText();
      }
    });
  }
  
  private void createUIComponents(){
  }

  public JPanel getOptionsPanel() {
    return myOptionsPanel;
  }

  public void init() {
  }

  @NotNull
  private Project getCurrentProject() {
    Project project = CommonDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext(myOptionsPanel));
    if (project == null) {
      project = ProjectManager.getInstance().getDefaultProject();
    }
    return project;
  }

  @Nullable
  private static PhpSdkAdditionalData getSdkAdditionalData(@NotNull Project project) {
    try {
      final PhpCSConfiguration configuration = PhpCSProjectConfiguration.getInstance(project).findSelectedConfiguration(project, false);
      if (configuration == null || StringUtil.isEmpty(configuration.getInterpreterId())) {
        return null;
      }

      final PhpInterpreter id = PhpInterpretersManagerImpl.getInstance(project).findInterpreterById(configuration.getInterpreterId());
      return id != null ? id.getPhpSdkAdditionalData() : null;
    }
    catch (QualityToolValidationException e) {
      return null;
    }
  }
}
