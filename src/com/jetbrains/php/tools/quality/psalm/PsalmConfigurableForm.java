package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.Version;
import com.jetbrains.php.PhpBundle;
import com.jetbrains.php.tools.quality.QualityToolConfigurableForm;
import com.jetbrains.php.tools.quality.QualityToolType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import static com.jetbrains.php.tools.quality.psalm.PsalmConfigurationBaseManager.PSALM;

public class PsalmConfigurableForm<C extends PsalmConfiguration> extends QualityToolConfigurableForm<C> {

  public PsalmConfigurableForm(@NotNull Project project, @NotNull C configuration) {
    super(project, configuration, PSALM, "psalm");
  }

  @Override
  public QualityToolType getQualityToolType() {
    return PsalmQualityToolType.INSTANCE;
  }

  @Override
  public String getHelpTopic() {
    return "reference.settings.php.Psalm";
  }

  @NotNull
  @Override
  public Pair<Boolean, String> validateMessage(@NonNls String message) {
    final Version version = extractVersion(message.trim().replaceFirst("Psalm.* v?([\\d.]*).*", "$1").trim());
    if ((version == null && !message.contains("dev-master")) || !message.contains(PSALM)) {
      return Pair.create(false, PhpBundle.message("quality.tool.can.not.determine.version", message));
    }
    return Pair.create(true, "OK, " + message);
  }

  @Override
  protected boolean validateWithNoAnsi() {
    return false;
  }
}