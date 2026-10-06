package com.jetbrains.php.tools.quality.psalm;

import com.google.gson.JsonElement;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.jetbrains.php.composer.actions.log.ComposerLogMessageBuilder;
import com.jetbrains.php.tools.quality.QualityToolConfigurationManager;
import com.jetbrains.php.tools.quality.QualityToolsComposerConfig;
import com.jetbrains.php.util.PhpOsUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.jetbrains.php.tools.quality.psalm.PsalmOpenSettingsProvider.PSALM_OPEN_SETTINGS_PROVIDER;

public class PsalmComposerConfig extends QualityToolsComposerConfig<PsalmConfiguration, PsalmValidationInspection> {
  private static final @NonNls String PACKAGE = "vimeo/psalm";
  private static final @NonNls String PSALM_XML = "psalm.xml";

  public PsalmComposerConfig() {
    super(PACKAGE);
  }

  @Override
  protected @NotNull String getRelativePath(@Nullable Project project) {
    return "bin/psalm" + (PhpOsUtil.isWindows(project) ? ".bat" : "");
  }

  @Override
  public String getQualityInspectionShortName() {
    return PsalmQualityToolType.INSTANCE.getInspectionId();
  }

  @Override
  protected boolean applyRulesetFromComposer(@NotNull Project project, PsalmConfiguration configuration) {
    final VirtualFile customRulesetFile = findComposerRulesetFile(project);
    return customRulesetFile != null && applyRuleset(project, customRulesetFile.getPath());
  }

  @Override
  protected boolean applyRulesetFromRoot(@NotNull Project project) {
    final VirtualFile customRulesetFile = findRootRulesetFile(project, PSALM_XML);
    return customRulesetFile != null && applyRuleset(project, customRulesetFile.getPath());
  }

  @Override
  protected void checkComposerScriptsLeaves(JsonElement element, Ref<String> result) {
    final String value = findScriptOptionValue(element.getAsString(), "psalm", "--config=", "-c");
    if (value != null) {
      result.set(value);
    }
  }

  @Override
  public @Nullable ComposerLogMessageBuilder.Settings getSettings() {
    return PSALM_OPEN_SETTINGS_PROVIDER;
  }

  private boolean applyRuleset(@NotNull Project project, @NotNull String customRuleset) {
    return modifyRulesetInspectionSetting(project, () -> PsalmOptionsConfiguration.getInstance(project).setConfig(customRuleset));
  }

  @Override
  public @NotNull QualityToolConfigurationManager<PsalmConfiguration> getConfigurationManager(@NotNull Project project) {
    return PsalmConfigurationManager.getInstance(project);
  }
}
