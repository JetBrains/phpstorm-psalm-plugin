package com.jetbrains.php.tools.quality.psalm.remote;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.util.xmlb.annotations.Tag;
import com.jetbrains.php.PhpBundle;
import com.jetbrains.php.config.interpreters.PhpInterpretersManagerImpl;
import com.jetbrains.php.config.interpreters.PhpSdkDependentConfiguration;
import com.jetbrains.php.tools.quality.psalm.PsalmConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.openapi.util.text.StringUtil.isEmpty;
import static com.jetbrains.php.tools.quality.QualityToolProjectConfiguration.DEFAULT_INTERPRETER_CONFIGURATION_ID;

@Tag("psalm_fixer_by_interpreter")
public class PsalmRemoteConfiguration extends PsalmConfiguration implements PhpSdkDependentConfiguration {
  private String myInterpreterId;

  @Override
  @Attribute("interpreter_id")
  public @Nullable @NlsSafe String getInterpreterId() {
    return myInterpreterId;
  }

  @Override
  public void setInterpreterId(@NotNull String interpreterId) {
    myInterpreterId = interpreterId;
  }

  @Override
  public @NotNull @NlsContexts.Label String getPresentableName(@Nullable Project project) {
    if (isCreatedAsDefaultInterpreterConfiguration()) return PhpBundle.message("quality.tools.label.by.default.project.interpreter");
    return getDefaultName(PhpInterpretersManagerImpl.getInstance(project).findInterpreterName(getInterpreterId()));
  }

  @Override
  public @NotNull String getId() {
    if (isCreatedAsDefaultInterpreterConfiguration()) return DEFAULT_INTERPRETER_CONFIGURATION_ID;
    final String interpreterId = getInterpreterId();
    return isEmpty(interpreterId) ? PhpBundle.message("undefined.interpreter") : interpreterId;
  }

  public static @NotNull @NlsContexts.Label String getDefaultName(@NlsContexts.Label @Nullable String interpreterName) {
    return isEmpty(interpreterName) ? PhpBundle.message("undefined.interpreter") : interpreterName;
  }

  @Override
  public PsalmRemoteConfiguration clone() {
    PsalmRemoteConfiguration settings = new PsalmRemoteConfiguration();
    settings.myInterpreterId = myInterpreterId;
    settings.setCreatedAsDefaultInterpreterConfiguration(this.isCreatedAsDefaultInterpreterConfiguration());
    settings.setDeletedFromTheList(this.isDeletedFromTheList());
    clone(settings);
    return settings;
  }

  @Override
  public String serialize(@Nullable String path) {
    return path;
  }

  @Override
  public String deserialize(@Nullable String path) {
    return path;
  }
}
