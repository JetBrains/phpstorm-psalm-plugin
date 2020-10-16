package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.jetbrains.php.tools.quality.QualityToolConfigurationManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsalmConfigurationManager extends QualityToolConfigurationManager<PsalmConfiguration> {

  public static final int DEFAULT_MAX_MESSAGES_PER_FILE = 50;

  public PsalmConfigurationManager(@Nullable Project project) {
    super(project);
    if (project != null) {
      myProjectManager = ServiceManager.getService(project, PsalmProjectConfigurationManager.class);
    }
    myApplicationManager = ApplicationManager.getApplication().getService(PsalmAppConfigurationManager.class);
  }

  public static PsalmConfigurationManager getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, PsalmConfigurationManager.class);
  }

  @State(name = "Psalm", storages = @Storage("php.xml"))
  static class PsalmProjectConfigurationManager extends PsalmConfigurationBaseManager {
  }

  @State(name = "Psalm", storages = @Storage("php.xml"))
  static class PsalmAppConfigurationManager extends PsalmConfigurationBaseManager {
  }
}
