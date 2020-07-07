package com.jetbrains.php.tools.quality.psalm;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.components.JBCheckBox;
import com.jetbrains.php.config.interpreters.PhpTextFieldWithSdkBasedBrowse;
import com.jetbrains.php.tools.quality.QualityToolCommonConfigurable;
import com.jetbrains.php.tools.quality.QualityToolsOptionsPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;

import static com.jetbrains.php.lang.inspections.PhpInspectionsUtil.createPanelWithSettingsLink;
import static com.jetbrains.php.tools.quality.psalm.PsalmConfigurationBaseManager.PSALM;

public class PsalmOptionsPanel extends QualityToolsOptionsPanel {
  private JPanel myOptionsPanel;
  private PhpTextFieldWithSdkBasedBrowse myConfigPathTextField;
  private JBCheckBox myShowInfoJBCheckBox;
  private JBCheckBox myFindUnusedCheckbox;
  private JBCheckBox myFindUnusedSuppressCheckbox;
  private JPanel myLinkPanel;
  private final PsalmValidationInspection myInspection;

  public PsalmOptionsPanel(PsalmValidationInspection inspection) {
    myInspection = inspection;
    DataManager.getInstance().getDataContextFromFocusAsync().onSuccess(context -> {
      Project project = CommonDataKeys.PROJECT.getData(context);
      if (project == null) {
        project = ProjectManager.getInstance().getDefaultProject();
      }
      myConfigPathTextField.setText(inspection.config);
      myConfigPathTextField
        .init(project, getSdkAdditionalData(project), PsalmBundle.message("psalm.configuration.file"), true, false);
    });
    myConfigPathTextField.getTextField().getDocument().addDocumentListener(new DocumentAdapter() {
      @Override
      protected void textChanged(@NotNull DocumentEvent e) {
        myInspection.config = myConfigPathTextField.getText();
      }
    });

    myShowInfoJBCheckBox.setSelected(inspection.showInfo);
    myShowInfoJBCheckBox.addActionListener(event -> myInspection.showInfo = myShowInfoJBCheckBox.isSelected());
    
    myFindUnusedCheckbox.setSelected(inspection.findUnusedCode);
    myFindUnusedCheckbox.addActionListener(event -> myInspection.findUnusedCode = myFindUnusedCheckbox.isSelected());
    
    myFindUnusedSuppressCheckbox.setSelected(inspection.findUnusedSuppress);
    myFindUnusedSuppressCheckbox.addActionListener(event -> myInspection.findUnusedSuppress = myFindUnusedSuppressCheckbox.isSelected());
  }
  
  private void createUIComponents(){
    myLinkPanel = createPanelWithSettingsLink(PSALM,
                                              QualityToolCommonConfigurable.class,
                                              QualityToolCommonConfigurable::new,
                                              i -> i.showConfigurable(PSALM));
  }

  @Override
  public JPanel getOptionsPanel() {
    return myOptionsPanel;
  }

  public void init() {
  }
}
