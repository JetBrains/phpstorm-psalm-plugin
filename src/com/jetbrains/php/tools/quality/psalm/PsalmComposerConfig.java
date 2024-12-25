package com.jetbrains.php.tools.quality.psalm;

import com.google.gson.JsonElement;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.profile.codeInspection.InspectionProfileManager;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import com.intellij.util.Consumer;
import com.jetbrains.php.composer.ComposerDataService;
import com.jetbrains.php.composer.actions.log.ComposerLogMessageBuilder;
import com.jetbrains.php.tools.quality.QualityToolConfigurationManager;
import com.jetbrains.php.tools.quality.QualityToolsComposerConfig;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.intellij.openapi.util.text.StringUtil.*;
import static com.jetbrains.php.tools.quality.psalm.PsalmOpenSettingsProvider.PSALM_OPEN_SETTINGS_PROVIDER;

public class PsalmComposerConfig extends QualityToolsComposerConfig<PsalmConfiguration, PsalmValidationInspection> {
  private static final @NonNls String PACKAGE = "vimeo/psalm";
  private static final @NonNls String RELATIVE_PATH = "bin/psalm" + (SystemInfo.isWindows ? ".bat" : "");
  private static final @NonNls String PSALM_XML = "psalm.xml";

  public PsalmComposerConfig() {
    super(PACKAGE, RELATIVE_PATH);
  }

  @Override
  public String getQualityInspectionShortName() {
    return PsalmQualityToolType.INSTANCE.getInspectionId();
  }

  @Override
  protected boolean applyRulesetFromComposer(@NotNull Project project, PsalmConfiguration configuration) {
    final String configPath = ComposerDataService.getInstance(project).getConfigPath();
    final VirtualFile config = LocalFileSystem.getInstance().refreshAndFindFileByPath(configPath);
    if (config == null) return false;

    final String ruleset = getRuleset(config);
    if (ruleset == null) return false;
    final VirtualFile customRulesetFile = detectCustomRulesetFile(config.getParent(), ruleset);
    if (customRulesetFile != null) {
      return modifyRulesetPsalmInspectionSetting(project, tool -> applyRuleset(project, customRulesetFile.getPath()));
    }
    return false;
  }

  protected boolean modifyRulesetPsalmInspectionSetting(@NotNull Project project, @NotNull Consumer<PsalmGlobalInspection> consumer) {
    VirtualFile projectDir = project.getBaseDir();
    if (projectDir == null) return false;

    final PsiDirectory file = PsiManager.getInstance(project).findDirectory(projectDir);
    if (file != null) {
      Key<PsalmGlobalInspection> key = Key.create(PsalmQualityToolType.INSTANCE.getInspectionId());
      InspectionProfileManager.getInstance(project).getCurrentProfile().modifyToolSettings(key, file, consumer);
      return true;
    }
    return false;
  }

  @Override
  protected void checkComposerScriptsLeaves(JsonElement element, Ref<String> result) {
    final String string = element.getAsString();
    if (string != null && string.contains("psalm")) {
      final List<String> split = split(string, " ");
      for (String arg: split) {
        final String prefix = "--config=";
        if (startsWith(arg, prefix)) {
          result.set(trimStart(arg, prefix));
          return;
        }
        final int index = split.indexOf(arg);
        if (StringUtil.equals(arg, "-c") && index < split.size() - 1) {
          result.set(split.get(index + 1));
          return;
        }
      }
    }
  }
  
  @Override
  protected boolean applyRulesetFromRoot(@NotNull Project project) {
    VirtualFile customRulesetFile = detectCustomRulesetFile(project.getBaseDir(), PSALM_XML);
    if(customRulesetFile == null){
      customRulesetFile = detectCustomRulesetFile(project.getBaseDir(), PSALM_XML + ".dist");
    }

    if (customRulesetFile != null) {
      final String path = customRulesetFile.getPath();
      return modifyRulesetPsalmInspectionSetting(project, tool -> applyRuleset(project, path));
    }
    return false;
  }

  @Override
  public @Nullable ComposerLogMessageBuilder.Settings getSettings() {
    return PSALM_OPEN_SETTINGS_PROVIDER;
  }

  private static void applyRuleset(Project project, String customRuleset) {
    PsalmOptionsConfiguration.getInstance(project).setConfig(customRuleset);
  }

  @Override
  public @NotNull QualityToolConfigurationManager<PsalmConfiguration> getConfigurationManager(@NotNull Project project) {
    return PsalmConfigurationManager.getInstance(project);
  }
}