package com.jetbrains.php.tools.quality.psalm;

import com.jetbrains.php.lang.inspections.PhpInspection;
import com.jetbrains.php.tools.quality.QualityToolAnnotator;
import com.jetbrains.php.tools.quality.QualityToolValidationInspection;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static com.intellij.openapi.util.text.StringUtilRt.isEmpty;
import static com.jetbrains.php.tools.quality.psalm.PsalmConfigurationBaseManager.PSALM;

public class PsalmValidationInspection extends QualityToolValidationInspection {
  public String config = "";

  @Override
  public String @NotNull [] getGroupPath() {
    return PhpInspection.GROUP_PATH_GENERAL;
  }

  @NotNull
  @Override
  public String getShortName() {
    return getClass().getSimpleName();
  }

  @Override
  public JComponent createOptionsPanel() {
    final PsalmOptionsPanel optionsPanel = new PsalmOptionsPanel(this);
    optionsPanel.init();
    return optionsPanel.getOptionsPanel();
  }

  @NotNull
  @Override
  protected QualityToolAnnotator getAnnotator() {
    return PsalmAnnotatorProxy.INSTANCE;
  }

  @Override
  public String getToolName() {
    return PSALM;
  }

  public List<String> getCommandLineOptions(String filePath) {
    @NonNls ArrayList<String> options = new ArrayList<>();
    options.add("--output-format=checkstyle");
    if (!isEmpty(config)) {
      options.add("--config=" + config);
    }
    options.add(filePath);
    return options;
  }
}
