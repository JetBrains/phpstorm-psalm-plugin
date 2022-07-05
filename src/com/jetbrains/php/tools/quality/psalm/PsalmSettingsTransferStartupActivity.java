package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.profile.codeInspection.InspectionProfileManager;
import org.jetbrains.annotations.NotNull;

public class PsalmSettingsTransferStartupActivity implements StartupActivity {
  @Override
  public void runActivity(@NotNull Project project) {
    if (project.isDefault()) {
      return;
    }
    Application app = ApplicationManager.getApplication();
    if (app.isUnitTestMode() || app.isHeadlessEnvironment()) {
      return;
    }

    PsalmGlobalInspection tool =
      (PsalmGlobalInspection)PsalmQualityToolType.INSTANCE.getGlobalTool(project,
                                                                         InspectionProfileManager.getInstance().getCurrentProfile());
    PsalmProjectConfiguration instance = PsalmProjectConfiguration.getInstance(project);
    if (tool != null && !tool.transferred) {
      instance.setConfig(tool.config);
      instance.setFindUnusedCode(tool.findUnusedCode);
      instance.setShowInfo(tool.showInfo);
      instance.setFindUnusedSuppress(tool.findUnusedSuppress);
      tool.transferred = true;
    }
  }
}
