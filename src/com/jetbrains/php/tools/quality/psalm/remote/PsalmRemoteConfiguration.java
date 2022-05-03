package com.jetbrains.php.tools.quality.psalm.remote;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.util.xmlb.annotations.Tag;
import com.jetbrains.php.PhpBundle;
import com.jetbrains.php.config.interpreters.PhpInterpretersManagerImpl;
import com.jetbrains.php.config.interpreters.PhpSdkDependentConfiguration;
import com.jetbrains.php.tools.quality.psalm.PsalmBundle;
import com.jetbrains.php.tools.quality.psalm.PsalmConfiguration;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.openapi.util.text.StringUtil.isEmpty;

@Tag("psalm_fixer_by_interpreter")
public class PsalmRemoteConfiguration extends PsalmConfiguration implements PhpSdkDependentConfiguration {
  private String myInterpreterId;

  @Override
  @Nullable
  @Attribute("interpreter_id")
  public @NlsSafe String getInterpreterId() {
    return myInterpreterId;
  }

  @Override
  public void setInterpreterId(@NotNull String interpreterId) {
    myInterpreterId = interpreterId;
  }

  @NotNull
  @Override
  public @NlsContexts.Label String getPresentableName(@Nullable Project project) {
    return getDefaultName(PhpInterpretersManagerImpl.getInstance(project).findInterpreterName(getInterpreterId()));
  }

  @NotNull
  @Override
  public @Nls String getId() {
    final String interpreterId = getInterpreterId();
    return isEmpty(interpreterId) ? PhpBundle.message("undefined.interpreter") : interpreterId;
  }

  @NotNull
  public static @NlsContexts.Label String getDefaultName(@NlsContexts.Label @Nullable String interpreterName) {
    return isEmpty(interpreterName) ? PhpBundle.message("undefined.interpreter") : interpreterName;
  }

  @Override
  public PsalmRemoteConfiguration clone() {
    PsalmRemoteConfiguration settings = new PsalmRemoteConfiguration();
    settings.myInterpreterId = myInterpreterId;
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
