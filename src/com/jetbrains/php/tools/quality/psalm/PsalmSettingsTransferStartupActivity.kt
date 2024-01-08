package com.jetbrains.php.tools.quality.psalm

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.profile.codeInspection.InspectionProfileManager
import com.intellij.util.PlatformUtils

class PsalmSettingsTransferStartupActivity : ProjectActivity {
  override suspend fun execute(project: Project) {
    if (project.isDefault) {
      return
    }
    val app = ApplicationManager.getApplication()
    if (app.isUnitTestMode || app.isHeadlessEnvironment || !PlatformUtils.isPhpStorm()) {
      return
    }

    val tool = PsalmQualityToolType.INSTANCE.getGlobalTool(project, InspectionProfileManager.getInstance(project).currentProfile) as? PsalmGlobalInspection
    val instance = PsalmOptionsConfiguration.getInstance(project)
    if (tool != null && !instance.isTransferred) {
      instance.config = tool.config
      instance.isFindUnusedCode = tool.findUnusedCode
      instance.isShowInfo = tool.showInfo
      instance.isFindUnusedSuppress = tool.findUnusedSuppress
      instance.isTransferred = true
    }
  }
}
