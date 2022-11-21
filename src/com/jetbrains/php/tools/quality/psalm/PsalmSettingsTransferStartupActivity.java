package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.profile.codeInspection.InspectionProfileManager;
import com.intellij.util.PlatformUtils;
import org.jetbrains.annotations.NotNull;

public class PsalmSettingsTransferStartupActivity implements StartupActivity {
  @Override
  public void runActivity(@NotNull Project project) {
    if (project.isDefault()) {
      return;
    }
    Application app = ApplicationManager.getApplication();
    if (app.isUnitTestMode() || app.isHeadlessEnvironment() || !PlatformUtils.isPhpStorm()) {
      return;
    }

    PsalmGlobalInspection tool =
      (PsalmGlobalInspection)PsalmQualityToolType.INSTANCE.getGlobalTool(project,
                                                                         InspectionProfileManager.getInstance(project).getCurrentProfile());
    PsalmOptionsConfiguration instance = PsalmOptionsConfiguration.getInstance(project);
    if (tool != null && !instance.isTransferred()) {
      instance.setConfig(tool.config);
      instance.setFindUnusedCode(tool.findUnusedCode);
      instance.setShowInfo(tool.showInfo);
      instance.setFindUnusedSuppress(tool.findUnusedSuppress);
      instance.setTransferred(true);
    }
  }
}
