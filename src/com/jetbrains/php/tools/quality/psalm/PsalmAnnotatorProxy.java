package com.jetbrains.php.tools.quality.psalm;

import com.intellij.execution.ExecutionException;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.util.ArrayUtil;
import com.jetbrains.php.PhpBundle;
import com.jetbrains.php.tools.quality.*;
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
  public final static PsalmAnnotatorProxy INSTANCE = new PsalmAnnotatorProxy();

  @Override
  @Nullable
  protected List<String> getOptions(@Nullable String filePath, @NotNull PsalmValidationInspection inspection, @NotNull Project project) {
    final PsalmGlobalInspection tool = (PsalmGlobalInspection)getQualityToolType().getGlobalTool((project));
    if (tool == null) {
      return emptyList();
    }
    return tool.getCommandLineOptions(filePath, project);
  }
  
  @Override
  protected void checkOptions(@NotNull List<String> options, @NotNull String workingDir, @NotNull Project project) {
    Path path = Paths.get(workingDir, "psalm.xml");
    if (!options.contains("-c") && !Files.exists(path)) {
      PsalmGlobalInspection.notifyAboutMissingConfig(project, path.toString());
    }
    else {
      path = Paths.get(options.get(options.indexOf("-c") + 1));
      path = Path.of(updateToLocalIfRemote(path.toString(), project, PsalmQualityToolType.INSTANCE));
      if (options.contains("-c") && !Files.exists(path) && FileUtil.isAncestor(project.getBasePath(), path.toString(), false)) {
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
          final QualityToolConfiguration configuration = getConfiguration(project, inspection);

          try {
            if (configuration != null) {
              getToolOutput(project, configuration.getInterpreterId(), configuration.getToolPath(),
                                                         configuration.getTimeout(), PhpBundle.message("cache.creating"), null,
                                                         ArrayUtil.toStringArray(getOptions(null, inspection, project)));
            }
          }
          catch (ExecutionException exception) {
            LOG.warn("PhpStormn couldn't create psalm cache");
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
  protected QualityToolMessageProcessor createMessageProcessor(@NotNull QualityToolAnnotatorInfo collectedInfo) {
    return new PsalmMessageProcessor(collectedInfo);
  }

  @Override
  protected void addAdditionalAnnotatorInfo(QualityToolAnnotatorInfo collectedInfo, QualityToolValidationInspection tool) {
  }

  @Override
  protected @NotNull QualityToolType<PsalmConfiguration> getQualityToolType() {
    return PsalmQualityToolType.INSTANCE;
  }
}

