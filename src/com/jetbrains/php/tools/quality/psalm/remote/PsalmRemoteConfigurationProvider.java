package com.jetbrains.php.tools.quality.psalm.remote;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.NullableFunction;
import com.intellij.util.PathMappingSettings;
import com.intellij.util.xmlb.XmlSerializer;
import com.jetbrains.php.config.interpreters.PhpInterpreter;
import com.jetbrains.php.config.interpreters.PhpInterpretersManagerImpl;
import com.jetbrains.php.config.interpreters.PhpSdkAdditionalData;
import com.jetbrains.php.remote.tools.quality.QualityToolByInterpreterConfigurableForm;
import com.jetbrains.php.remote.tools.quality.QualityToolByInterpreterDialog;
import com.jetbrains.php.run.remote.PhpRemoteInterpreterManager;
import com.jetbrains.php.tools.quality.QualityToolConfigurableForm;
import com.jetbrains.php.tools.quality.psalm.PsalmConfigurableForm;
import com.jetbrains.php.tools.quality.psalm.PsalmConfiguration;
import com.jetbrains.php.tools.quality.psalm.PsalmConfigurationManager;
import com.jetbrains.php.tools.quality.psalm.PsalmConfigurationProvider;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.intellij.openapi.util.text.StringUtil.isNotEmpty;
import static com.jetbrains.php.tools.quality.psalm.PsalmConfigurationBaseManager.PSALM;

public class PsalmRemoteConfigurationProvider extends PsalmConfigurationProvider {

  @NonNls private static final String PSALM_BY_INTERPRETER = "psalm_by_interpreter";

  @Override
  public boolean canLoad(@NotNull String tagName) {
    return StringUtil.equals(tagName, PSALM_BY_INTERPRETER);
  }

  @Nullable
  @Override
  public PsalmConfiguration load(@NotNull Element element) {
    return XmlSerializer.deserialize(element, PsalmRemoteConfiguration.class);
  }

  @Nullable
  @Override
  public QualityToolConfigurableForm<PsalmRemoteConfiguration> createConfigurationForm(@NotNull Project project,
                                                                                            @NotNull PsalmConfiguration settings) {
    if (settings instanceof PsalmRemoteConfiguration) {
      final PsalmRemoteConfiguration remoteConfiguration = (PsalmRemoteConfiguration)settings;
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
    final QualityToolByInterpreterDialog<PsalmConfiguration, PsalmRemoteConfiguration>
      dialog = new QualityToolByInterpreterDialog<>(project, existingSettings, PSALM, PsalmRemoteConfiguration.class);
    if (dialog.showAndGet()) {
      final String id = PhpInterpretersManagerImpl.getInstance(project).findInterpreterId(dialog.getSelectedInterpreterName());
      if (isNotEmpty(id)) {
        final PsalmRemoteConfiguration settings = new PsalmRemoteConfiguration();
        settings.setInterpreterId(id);

        final PhpSdkAdditionalData data = PhpInterpretersManagerImpl.getInstance(project).findInterpreterDataById(id);
        final PhpRemoteInterpreterManager manager = PhpRemoteInterpreterManager.getInstance();
        if (manager != null && data != null && project != null) {
          final PathMappingSettings mappings = manager.createPathMappings(project, data);
          fillSettingsByDefaultValue(settings, PsalmConfigurationManager.getInstance(project).getLocalSettings(),
                                     localPath -> localPath == null ? null : mappings.convertToRemote(localPath));
        }
        return settings;
      }
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
    settings.setTimeout(30000);
  }
}
