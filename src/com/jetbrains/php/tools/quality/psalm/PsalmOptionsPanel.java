package com.jetbrains.php.tools.quality.psalm;

import javax.swing.*;

public class PsalmOptionsPanel {
  private JPanel myOptionsPanel;
  private final PsalmValidationInspection myInspection;

  public PsalmOptionsPanel(PsalmValidationInspection inspection) {
    myInspection = inspection;
  }
  
  private void createUIComponents(){
  }

  public JPanel getOptionsPanel() {
    return myOptionsPanel;
  }

  public void init() {
  }
}
