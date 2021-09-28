package com.jetbrains.php.tools.quality.psalm;

import com.intellij.codeInspection.*;
import com.intellij.codeInspection.ex.ExternalAnnotatorBatchInspection;
import com.intellij.execution.ExecutionException;
import com.intellij.notification.Notification;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.profile.codeInspection.InspectionProfileManager;
import com.intellij.profile.codeInspection.ProjectInspectionProfileManager;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.jetbrains.php.tools.quality.*;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.intellij.codeInspection.ex.EditInspectionToolsSettingsAction.editToolSettings;
import static com.intellij.notification.NotificationType.WARNING;
import static com.intellij.openapi.util.text.StringUtilRt.isEmpty;
import static com.intellij.openapi.vfs.VfsUtil.markDirtyAndRefresh;
import static com.jetbrains.php.tools.quality.QualityToolAnnotator.*;
import static com.jetbrains.php.tools.quality.QualityToolProcessCreator.getToolOutput;

public class PsalmGlobalInspection extends QualityToolValidationGlobalInspection implements ExternalAnnotatorBatchInspection {
  public @NlsSafe String config = "";
  public boolean showInfo = false;
  public boolean findUnusedCode = false;
  public boolean findUnusedSuppress = false;
  public static final Key<List<QualityToolXmlMessageProcessor.ProblemDescription>> PSALM_ANNOTATOR_INFO = Key.create("ANNOTATOR_INFO_PSALM");

  @Override
  public JComponent createOptionsPanel() {
    final PsalmOptionsPanel optionsPanel = new PsalmOptionsPanel(this);
    optionsPanel.init();
    return optionsPanel.getOptionsPanel();
  }

  @Override
  protected void checkCmdOptions(@NotNull Project project) {
    final List<String> options = getCommandLineOptions(null, project);
    Path path = Paths.get(project.getBasePath(), "psalm.xml");
    if (!options.contains("-c") && !Files.exists(path)) {
      notifyAboutMissingConfig(project, path.toString());
    }
    else {
      path = Paths.get(options.get(options.indexOf("-c") + 1));
      path = Path.of(updateToLocalIfRemote(path.toString(),project, PsalmQualityToolType.INSTANCE));
      if (options.contains("-c") && !Files.exists(path)) {
        notifyAboutMissingConfig(project, path.toString());
      }
    }
  }

  @Override
  public void inspectionStarted(@NotNull InspectionManager manager,
                                @NotNull GlobalInspectionContext globalContext,
                                @NotNull ProblemDescriptionsProcessor problemDescriptionsProcessor) {
    super.inspectionStarted(manager, globalContext, problemDescriptionsProcessor);
    final QualityToolAnnotator annotator = getAnnotator();
    final QualityToolAnnotatorInfo info =
      annotator.collectAnnotatorInfo(null, null, globalContext.getProject(), ((InspectionManagerBase)manager).getCurrentProfile(), false);
    if (info != null) {
      manager.getProject().putUserData(ANNOTATOR_INFO, annotator.doAnnotate(info));
    }
  }

  @Override
  public ProblemDescriptor @NotNull [] checkFile(@NotNull PsiFile file,
                                                 @NotNull GlobalInspectionContext context,
                                                 @NotNull InspectionManager manager) {
    ProblemsHolder holder = new ProblemsHolder(manager, file, false);
    super.checkFile(file, manager, holder, context, null);
    return holder.getResultsArray();
  }

  public static void notifyAboutMissingConfig(@NotNull Project project, @NotNull String path) {
    final Notification notification =
      new Notification(GROUP_ID, PsalmQualityToolType.INSTANCE.getDisplayName(),
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
          final Path configPath = Paths.get(project.getBasePath(), "psalm.xml");
          markDirtyAndRefresh(true, false, false, new File(configPath.toString()));
          updateInspectionSettings(configPath);
          notification.expire();
        }
        catch (QualityToolValidationException | ExecutionException exception) {
          Notifications.Bus.notify(new Notification(GROUP_ID, PsalmQualityToolType.INSTANCE.getDisplayName(),
                                                  PsalmBundle.message("psalm.config.not.generated"), WARNING));
          
        }
      }

      private void updateInspectionSettings(Path configPath) {
        VirtualFile projectDir = project.getBaseDir();
        if (projectDir == null) return;
        final PsiDirectory file = PsiManager.getInstance(project).findDirectory(projectDir);
        if (file == null) return;
        InspectionProfileManager.getInstance(project).getCurrentProfile().modifyToolSettings(
          Key.<PsalmGlobalInspection>create(PsalmQualityToolType.INSTANCE.getInspectionId()), file,
          inspection -> inspection.config = configPath.toString());
      }
    });
    Notifications.Bus.notify(notification);
  }

  @Override
  public @Nullable LocalInspectionTool getSharedLocalInspectionTool() {
    return new PsalmValidationInspection();
  }

  @Override
  protected @NotNull QualityToolAnnotator getAnnotator() {
    return PsalmAnnotatorProxy.INSTANCE;
  }

  @Override
  protected Key<List<QualityToolXmlMessageProcessor.ProblemDescription>> getKey() {
    return PSALM_ANNOTATOR_INFO;
  }


  public List<String> getCommandLineOptions(@Nullable String filePath, Project project) {
    @NonNls ArrayList<String> options = new ArrayList<>();
    options.add("--output-format=checkstyle");
    if (!isEmpty(config)) {
      options.add("-c");
      options.add(updateIfRemote(config, project, PsalmQualityToolType.INSTANCE));
    }
    if (showInfo) {
      options.add("--show-info=true");
    }
    if (findUnusedCode) {
      options.add("--find-unused-code");
    }
    if (findUnusedSuppress) {
      options.add("--find-unused-psalm-suppress");
    }
    options.add("--monochrome");
    if (filePath != null) {
      options.add(filePath);
    }
    return options;
  }
}
