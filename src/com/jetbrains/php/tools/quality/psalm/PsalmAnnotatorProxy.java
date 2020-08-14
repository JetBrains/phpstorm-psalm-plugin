package com.jetbrains.php.tools.quality.psalm;

import com.intellij.notification.Notification;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;
import com.jetbrains.php.tools.quality.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.intellij.notification.NotificationType.WARNING;
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
  protected void checkOptions(@NotNull List<String> options, @NotNull String workingDir, @NotNull QualityToolMessageProcessor processor) {
    if (!options.contains("-c") && !Files.exists(Paths.get(workingDir, "psalm.xml"))) {
      Notifications.Bus
        .notify(new Notification(GROUP_ID, getQualityToolType().getDisplayName(), PsalmBundle.message("psalm.config.not.found", workingDir),
                                 WARNING));
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

