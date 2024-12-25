package com.jetbrains.php.tools.quality.psalm.remote;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.NullableFunction;
import com.intellij.util.xmlb.XmlSerializer;
import com.jetbrains.php.config.interpreters.PhpInterpreter;
import com.jetbrains.php.config.interpreters.PhpInterpretersManagerImpl;
import com.jetbrains.php.config.interpreters.PhpSdkAdditionalData;
import com.jetbrains.php.remote.interpreter.PhpRemoteSdkAdditionalData;
import com.jetbrains.php.remote.tools.quality.QualityToolByInterpreterConfigurableForm;
import com.jetbrains.php.remote.tools.quality.QualityToolByInterpreterDialog;
import com.jetbrains.php.tools.quality.QualityToolConfigurableForm;
import com.jetbrains.php.tools.quality.psalm.*;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.intellij.openapi.util.text.StringUtil.isNotEmpty;
import static com.jetbrains.php.remote.tools.quality.QualityToolByInterpreterDialog.getLocalOrDefaultInterpreterConfiguration;
import static com.jetbrains.php.tools.quality.psalm.PsalmConfigurationBaseManager.PSALM;

public class PsalmRemoteConfigurationProvider extends PsalmConfigurationProvider {

  private static final @NonNls String PSALM_BY_INTERPRETER = "psalm_fixer_by_interpreter";

  @Override
  public boolean canLoad(@NotNull String tagName) {
    return StringUtil.equals(tagName, PSALM_BY_INTERPRETER);
  }

  @Override
  public @Nullable PsalmConfiguration load(@NotNull Element element) {
    return XmlSerializer.deserialize(element, PsalmRemoteConfiguration.class);
  }

  @Override
  public @Nullable QualityToolConfigurableForm<PsalmRemoteConfiguration> createConfigurationForm(@NotNull Project project,
                                                                                                 @NotNull PsalmConfiguration settings) {
    if (settings instanceof PsalmRemoteConfiguration remoteConfiguration) {
      final PsalmConfigurableForm<PsalmRemoteConfiguration> delegate =
        new PsalmConfigurableForm<>(project, remoteConfiguration);
      return new QualityToolByInterpreterConfigurableForm<>(project, remoteConfiguration, delegate) {
        @Override
        protected boolean validateWithNoAnsi() {
          return false;
        }
      };
    }
    return null;
  }

  @Override
  public PsalmConfiguration createNewInstance(@Nullable Project project, @NotNull List<PsalmConfiguration> existingSettings) {
    var dialog =
      new QualityToolByInterpreterDialog<>(project, existingSettings, PSALM, PsalmRemoteConfiguration.class, PsalmQualityToolType.INSTANCE);
    if (dialog.showAndGet()) {
      final String id = PhpInterpretersManagerImpl.getInstance(project).findInterpreterId(dialog.getSelectedInterpreterName());
      if (isNotEmpty(id)) {
        final PsalmRemoteConfiguration settings = new PsalmRemoteConfiguration();
        settings.setInterpreterId(id);

        final PhpSdkAdditionalData data = PhpInterpretersManagerImpl.getInstance(project).findInterpreterDataById(id);
        fillDefaultSettings(project, settings, PsalmConfigurationManager.getInstance(project).getOrCreateLocalSettings(), data, data instanceof PhpRemoteSdkAdditionalData);

        return settings;
      }
      return (PsalmConfiguration)getLocalOrDefaultInterpreterConfiguration(dialog.getSelectedInterpreterName(), project, PsalmQualityToolType.INSTANCE);
    }
    return null;
  }

  @Override
  public PsalmConfiguration createConfigurationByInterpreter(@NotNull PhpInterpreter interpreter) {
    final PsalmRemoteConfiguration settings = new PsalmRemoteConfiguration();
    settings.setInterpreterId(interpreter.getId());
    return settings;
  }

  @Override
  protected void fillSettingsByDefaultValue(@NotNull PsalmConfiguration settings,
                                            @NotNull PsalmConfiguration localConfiguration,
                                            @NotNull NullableFunction<String, String> preparePath) {
    super.fillSettingsByDefaultValue(settings, localConfiguration, preparePath);
    settings.setTimeout(60000);
  }
}
