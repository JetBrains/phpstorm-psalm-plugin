package com.jetbrains.php.tools.quality.psalm;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.execution.ExecutionException;
import com.intellij.notification.Notification;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.profile.codeInspection.InspectionProfileManager;
import com.intellij.profile.codeInspection.ProjectInspectionProfileManager;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import com.jetbrains.php.tools.quality.ParsedProblem;
import com.jetbrains.php.tools.quality.QualityToolValidationException;
import com.jetbrains.php.tools.quality.QualityToolValidationGlobalInspection;
import com.jetbrains.php.util.PhpPathUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.intellij.codeInspection.ex.EditInspectionToolsSettingsAction.editToolSettings;
import static com.intellij.notification.NotificationType.WARNING;
import static com.intellij.openapi.util.text.StringUtilRt.isEmpty;
import static com.intellij.openapi.vfs.VfsUtil.markDirtyAndRefresh;
import static com.jetbrains.php.tools.quality.QualityToolAnnotator.updateIfRemoteMappingExists;
import static com.jetbrains.php.tools.quality.QualityToolAnnotator.updateToLocalIfRemote;
import static com.jetbrains.php.tools.quality.QualityToolErrorReporterKt.PHP_QUALITY_TOOLS_NOTIFICATION_GROUP;
import static com.jetbrains.php.tools.quality.QualityToolProcessCreator.getToolOutput;

public class PsalmGlobalInspection extends QualityToolValidationGlobalInspection {
  public @NlsSafe String config = "";
  public boolean showInfo = false;
  public boolean findUnusedCode = false;
  public boolean findUnusedSuppress = false;
  public static final Key<List<ParsedProblem>> PSALM_ANNOTATOR_INFO = Key.create("ANNOTATOR_INFO_PSALM");

  @Override
  protected void checkCmdOptions(@NotNull Project project) {
    final String basePath = project.getBasePath();
    if (basePath == null) return;
    final List<String> options = getBaseCommandLineOptions(null, project);
    final int configOptionIndex = options.indexOf("-c");
    if (configOptionIndex < 0 || configOptionIndex + 1 >= options.size()) {
      final Path defaultPath = Paths.get(basePath, "psalm.xml");
      if (!Files.exists(defaultPath)) {
        notifyAboutMissingConfig(project, defaultPath.toString());
      }
      return;
    }
    final Path path = PhpPathUtils.tryParsePath(
      updateToLocalIfRemote(Paths.get(options.get(configOptionIndex + 1)).toString(), project, PsalmQualityToolType.INSTANCE));
    if (path != null && !Files.exists(path)) {
      notifyAboutMissingConfig(project, path.toString());
    }
  }

  public static void notifyAboutMissingConfig(@NotNull Project project, @NotNull String path) {
    final Notification notification =
      new Notification(PHP_QUALITY_TOOLS_NOTIFICATION_GROUP, PsalmQualityToolType.INSTANCE.getDisplayName(),
                       PsalmBundle.message("psalm.config.not.found", path), WARNING);

    notification.addAction(new AnAction(PsalmBundle.message("action.show.inspection.settings.text")) {
      @Override
      public void actionPerformed(@NotNull AnActionEvent e) {
        editToolSettings(project, ProjectInspectionProfileManager.getInstance(project).getCurrentProfile(),
                         PsalmQualityToolType.INSTANCE.getInspectionShortName(project));
        notification.expire();
      }
    });
    //noinspection DialogTitleCapitalization
    notification.addAction(new AnAction(PsalmBundle.message("action.generate.psalm.xml.in.project.root.text")) {
      @Override
      public void actionPerformed(@NotNull AnActionEvent e) {
        try {
          final PsalmConfiguration configuration = PsalmProjectConfiguration.getInstance(project).findSelectedConfiguration(project, false);
          if (configuration == null) {
            return;
          }
          getToolOutput(project, configuration.getInterpreterId(), configuration.getToolPath(), configuration.getTimeout(),
                        PsalmBundle.message("action.generate.psalm.xml.in.project.root"), null, "--init", ".", "3");
          final String basePath = project.getBasePath();
          if (basePath == null) return;
          final Path configPath = Paths.get(basePath, "psalm.xml");
          markDirtyAndRefresh(true, false, false, configPath);
          updateInspectionSettings(configPath);
          notification.expire();
        }
        catch (QualityToolValidationException | ExecutionException exception) {
          Notifications.Bus.notify(new Notification(PHP_QUALITY_TOOLS_NOTIFICATION_GROUP, PsalmQualityToolType.INSTANCE.getDisplayName(),
                                                  PsalmBundle.message("psalm.config.not.generated"), WARNING));
          
        }
      }

      private void updateInspectionSettings(Path configPath) {
        VirtualFile projectDir = ProjectUtil.guessProjectDir(project);
        if (projectDir == null) return;
        final PsiDirectory file = PsiManager.getInstance(project).findDirectory(projectDir);
        if (file == null) return;
        InspectionProfileManager.getInstance(project).getCurrentProfile().modifyToolSettings(
          Key.<PsalmGlobalInspection>create(PsalmQualityToolType.INSTANCE.getInspectionId()), file,
          inspection -> PsalmOptionsConfiguration.getInstance(project).setConfig(configPath.toString()));
      }
    });
    Notifications.Bus.notify(notification);
  }

  @Override
  public @Nullable LocalInspectionTool getSharedLocalInspectionTool() {
    return new PsalmValidationInspection();
  }

  @Override
  protected @NotNull PsalmAnnotatorProxy getAnnotator() {
    return PsalmAnnotatorProxy.INSTANCE;
  }

  @Override
  protected Key<List<ParsedProblem>> getKey() {
    return PSALM_ANNOTATOR_INFO;
  }

  public static List<String> getBaseCommandLineOptions(@Nullable String filePath, Project project) {
    PsalmOptionsConfiguration configuration = PsalmOptionsConfiguration.getInstance(project);
    @NonNls ArrayList<String> options = new ArrayList<>();
    if (!isEmpty(configuration.getConfig())) {
      options.add("-c");
      options.add(updateIfRemoteMappingExists(configuration.getConfig(), project, PsalmQualityToolType.INSTANCE));
    }
    if (configuration.isShowInfo()) {
      options.add("--show-info=true");
    }
    if (configuration.isFindUnusedCode()) {
      options.add("--find-unused-code");
    }
    if (configuration.isFindUnusedSuppress()) {
      options.add("--find-unused-psalm-suppress");
    }
    if (filePath != null) {
      options.add(updateIfRemoteMappingExists(filePath, project, PsalmQualityToolType.INSTANCE));
    }
    return options;
  }
}
