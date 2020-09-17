package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.project.Project;
import com.jetbrains.php.tools.quality.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.intellij.util.containers.ContainerUtil.emptyList;

public class PsalmAnnotatorProxy extends QualityToolAnnotator<PsalmValidationInspection> {
  public final static PsalmAnnotatorProxy INSTANCE = new PsalmAnnotatorProxy();

  @Override
  @Nullable
  protected List<String> getOptions(@Nullable String filePath, @NotNull PsalmValidationInspection inspection, @NotNull Project project) {
    final PsalmGlobalInspection tool = (PsalmGlobalInspection)getQualityToolType().getGlobalTool((project));
    if (tool == null) {
      return emptyList();
    }
    return tool.getCommandLineOptions(filePath);
  }
  
  @Override
  protected void checkOptions(@NotNull List<String> options, @NotNull String workingDir, @NotNull Project project) {
    Path path = Paths.get(workingDir, "psalm.xml");
    if (!options.contains("-c") && !Files.exists(path)) {
      PsalmGlobalInspection.notifyAboutMissingConfig(project, path.toString());
    }
    else {
      path = Paths.get(options.get(options.indexOf("-c") + 1));
      if (options.contains("-c") && !Files.exists(path)) {
        PsalmGlobalInspection.notifyAboutMissingConfig(project, path.toString());
      }
    }
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

