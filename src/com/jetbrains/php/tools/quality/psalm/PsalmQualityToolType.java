package com.jetbrains.php.tools.quality.psalm;

import com.intellij.codeInspection.InspectionProfile;
import com.intellij.codeInspection.ex.InspectionToolWrapper;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.profile.codeInspection.InspectionProjectProfileManager;
import com.jetbrains.php.tools.quality.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.util.ObjectUtils.tryCast;
import static com.jetbrains.php.tools.quality.psalm.PsalmConfigurationBaseManager.PSALM;

public final class PsalmQualityToolType extends QualityToolType<PsalmConfiguration> {
  public static final PsalmQualityToolType INSTANCE = new PsalmQualityToolType();

  private PsalmQualityToolType() {
  }

  @Override
  public @NotNull String getDisplayName() {
    return PSALM;
  }

  @Override
  public @NotNull QualityToolBlackList getQualityToolBlackList(@NotNull Project project) {
    return PsalmBlackList.getInstance(project);
  }

  @Override
  public @NotNull QualityToolConfigurationManager<PsalmConfiguration> getConfigurationManager(@NotNull Project project) {
    return PsalmConfigurationManager.getInstance(project);
  }

  @Override
  protected @NotNull PsalmValidationInspection getInspection() {
    return new PsalmValidationInspection();
  }

  @Override
  protected @Nullable QualityToolConfigurationProvider<PsalmConfiguration> getConfigurationProvider() {
    return PsalmConfigurationProvider.getInstances();
  }

  @Override
  protected @NotNull QualityToolConfigurableForm<PsalmConfiguration> createConfigurableForm(@NotNull Project project,
                                                                                              PsalmConfiguration settings) {
    return new PsalmConfigurableForm<>(project, settings);
  }

  @Override
  protected @NotNull Configurable getToolConfigurable(@NotNull Project project) {
    return new PsalmConfigurable(project);
  }

  @Override
  protected @NotNull QualityToolProjectConfiguration<PsalmConfiguration> getProjectConfiguration(@NotNull Project project) {
    return PsalmProjectConfiguration.getInstance(project);
  }

  @Override
  protected @NotNull PsalmConfiguration createConfiguration() {
    return new PsalmConfiguration();
  }

  @Override
  public @NotNull String getHelpTopic() {
    return "reference.settings.php.Psalm";
  }
  
  @Override
  public QualityToolValidationGlobalInspection getGlobalTool(@NotNull Project project,
                                                             @Nullable InspectionProfile profile) {
    if (profile == null) {
      profile = InspectionProjectProfileManager.getInstance(project).getCurrentProfile();
    }
    final InspectionToolWrapper<?, ?> inspectionTool = profile.getInspectionTool(getInspectionId(), project);
    if (inspectionTool == null) {
      return null;
    }
    return tryCast(inspectionTool.getTool(), PsalmGlobalInspection.class);
  }

  @Override
  public String getInspectionId() {
    return "PsalmGlobal";
  }

  @Override
  public String getInspectionShortName(@NotNull Project project) {
    final QualityToolValidationGlobalInspection tool = getGlobalTool(project, null);
    if (tool != null) {
      return tool.getShortName();
    }
    return getInspection().getShortName();
  }
}
