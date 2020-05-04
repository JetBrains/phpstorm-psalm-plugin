package com.jetbrains.php.tools.quality.psalm;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.execution.ExecutionException;
import com.intellij.openapi.project.Project;
import com.jetbrains.php.config.interpreters.PhpSdkFileTransfer;
import com.jetbrains.php.tools.quality.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.jetbrains.php.tools.quality.QualityToolProcessCreator.runToolProcess;

public class PsalmAnnotatorProxy extends QualityToolAnnotator {
  public final static PsalmAnnotatorProxy INSTANCE = new PsalmAnnotatorProxy();

  @Nullable
  @Override
  protected QualityToolConfiguration getConfiguration(@NotNull Project project, @NotNull LocalInspectionTool inspection) {
    try {
      return PsalmProjectConfiguration.getInstance(project).findSelectedConfiguration(project);
    }
    catch (QualityToolValidationException ignore) {
    }
    return null;
  }

  @NotNull
  @Override
  protected String getInspectionId() {
    return new PsalmValidationInspection().getID();
  }

  @Override
  protected void runTool(@NotNull QualityToolMessageProcessor messageProcessor,
                         @NotNull final QualityToolAnnotatorInfo collectedInfo,
                         @NotNull PhpSdkFileTransfer transfer) throws ExecutionException {
    PsalmValidationInspection inspection = (PsalmValidationInspection)collectedInfo.getInspection();

    final PsalmBlackList blackList = PsalmBlackList.getInstance(collectedInfo.getProject());
    runToolProcess(collectedInfo, blackList, messageProcessor, collectedInfo.getProject().getBasePath(), transfer,
                   inspection.getCommandLineOptions(collectedInfo.getOriginalFile().getPath()));
    if (messageProcessor.getInternalErrorMessage() != null && collectedInfo.isOnTheFly()) {
      if (collectedInfo.isOnTheFly()) {
        final String message = messageProcessor.getInternalErrorMessage().getMessageText();
        showProcessErrorMessage(collectedInfo, blackList, message);
        logWarning(collectedInfo, message, null);
      }
      messageProcessor.setFatalError();
    }
  }

  @Override
  protected QualityToolMessageProcessor createMessageProcessor(@NotNull QualityToolAnnotatorInfo collectedInfo) {
    return new PsalmMessageProcessor(collectedInfo);
  }

  @Override
  protected void addAdditionalAnnotatorInfo(QualityToolAnnotatorInfo collectedInfo, QualityToolValidationInspection tool) {
  }
}

