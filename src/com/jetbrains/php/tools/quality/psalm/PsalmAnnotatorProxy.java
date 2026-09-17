package com.jetbrains.php.tools.quality.psalm;

import com.intellij.codeInspection.InspectionProfile;
import com.intellij.execution.ExecutionException;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.profile.codeInspection.InspectionProjectProfileManager;
import com.intellij.util.ArrayUtil;
import com.jetbrains.php.PhpBundle;
import com.jetbrains.php.tools.quality.QualityToolAnnotator;
import com.jetbrains.php.tools.quality.QualityToolAnnotatorInfo;
import com.jetbrains.php.tools.quality.QualityToolConfiguration;
import com.jetbrains.php.tools.quality.QualityToolMessageProcessor;
import com.jetbrains.php.tools.quality.QualityToolType;
import com.jetbrains.php.util.PhpPathUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static com.intellij.util.containers.ContainerUtil.emptyList;
import static com.jetbrains.php.tools.quality.QualityToolProcessCreator.getToolOutput;

public class PsalmAnnotatorProxy extends QualityToolAnnotator<PsalmValidationInspection> {
  private static final Logger LOG = Logger.getInstance(PsalmAnnotatorProxy.class);
  public static final PsalmAnnotatorProxy INSTANCE = new PsalmAnnotatorProxy();

  protected static @Nullable List<String> getBaseOptions(@Nullable String filePath, @NotNull Project project,
                                                          @Nullable InspectionProfile profile) {
    if (PsalmQualityToolType.INSTANCE.getGlobalTool(project, profile) == null) return null;
    return PsalmGlobalInspection.getBaseCommandLineOptions(filePath, project);
  }

  @Override
  protected @Nullable List<String> getOptions(@Nullable String filePath, @NotNull QualityToolAnnotatorInfo<PsalmValidationInspection> info) {
    List<String> options = getBaseOptions(filePath, info.getProject(), info.getProfile());
    if (options == null) return emptyList();
    options.add("--output-format=checkstyle");
    options.add("--monochrome");
    return options;
  }
  
  @Override
  protected void checkOptions(@NotNull List<String> options, @NotNull String workingDir, @NotNull Project project) {
    Path path = Paths.get(workingDir, "psalm.xml");
    if (!options.contains("-c") && !Files.exists(path)) {
      PsalmGlobalInspection.notifyAboutMissingConfig(project, path.toString());
    }
    else {
      path = Paths.get(options.get(options.indexOf("-c") + 1));
      path = PhpPathUtils.tryParsePath(updateToLocalIfRemote(path.toString(), project, PsalmQualityToolType.INSTANCE));
      if (options.contains("-c") && path != null && !Files.exists(path) && FileUtil.isAncestor(project.getBasePath(), path.toString(), false)) {
        PsalmGlobalInspection.notifyAboutMissingConfig(project, path.toString());
      }
    }
  }

  @Override
  protected List<AnAction> getAdditionalTimeoutActions() {
    return Collections.singletonList(new AnAction(PhpBundle.message("init.cache")) {
      @Override
      public void actionPerformed(@NotNull AnActionEvent e) {
        final Project project = e.getProject();
        if (project != null && !project.isDisposed()) {
          final PsalmValidationInspection inspection = new PsalmValidationInspection();
          final QualityToolConfiguration configuration = getConfiguration(project);

          try {
            if (configuration != null) {
              InspectionProfile profile = InspectionProjectProfileManager.getInstance(project).getCurrentProfile();
              QualityToolAnnotatorInfo<PsalmValidationInspection> info =
                createAnnotatorInfo(null, inspection, profile, project, configuration, false);
              getToolOutput(project, configuration.getInterpreterId(), configuration.getToolPath(),
                            configuration.getTimeout(), PhpBundle.message("cache.creating"), null,
                            ArrayUtil.toStringArray(getOptions(null, info)));
            }
          }
          catch (ExecutionException exception) {
            LOG.warn("PhpStorm couldn't create psalm cache");
          }
        }
      }
    });
  }

  @Override
  public String getPairedBatchInspectionShortName() {
    return getQualityToolType().getInspectionId();
  }

  @Override
  protected QualityToolMessageProcessor createMessageProcessor(@NotNull QualityToolAnnotatorInfo<PsalmValidationInspection> collectedInfo) {
    return new PsalmMessageProcessor(collectedInfo);
  }

  @Override
  protected @NotNull QualityToolType<PsalmConfiguration> getQualityToolType() {
    return PsalmQualityToolType.INSTANCE;
  }

}
