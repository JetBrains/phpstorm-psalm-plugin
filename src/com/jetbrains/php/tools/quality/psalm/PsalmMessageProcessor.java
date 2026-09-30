package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.jetbrains.php.tools.quality.QualityToolAnnotator;
import com.jetbrains.php.tools.quality.QualityToolAnnotatorInfo;
import com.jetbrains.php.tools.quality.QualityToolMessage;
import com.jetbrains.php.tools.quality.QualityToolXmlMessageProcessor;
import com.jetbrains.php.util.PhpPathUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.intellij.openapi.util.io.FileUtilRt.toSystemIndependentName;
import static com.jetbrains.php.tools.quality.QualityToolMessage.Severity.ERROR;
import static com.jetbrains.php.tools.quality.QualityToolMessage.Severity.WARNING;
import static com.jetbrains.php.tools.quality.psalm.PsalmGlobalInspection.PSALM_ANNOTATOR_INFO;
import static java.util.Collections.singletonList;

public class PsalmMessageProcessor extends QualityToolXmlMessageProcessor {
  private static final @NonNls String WARNING_MESSAGE_START = "<file name=";
  private static final @NonNls String WARNING_MESSAGE_END = "</file>";
  private static final @NonNls String LINE_NUMBER_ATTR = "line";
  private static final @NonNls String COLUMN_NUMBER_ATTR = "column";
  private static final String MESSAGE_ATTR = "message";
  private static final @NonNls String SEVERITY_ATTR = "severity";
  private static final Pattern MISSING_FILE_MESSAGE = Pattern.compile("Cannot find file (.+?) to include");
  final Project myProject;
  private final @Nullable TempFolderMapping myTempFolderMapping;

  protected PsalmMessageProcessor(QualityToolAnnotatorInfo<?> info) {
    super(info);
    myProject = info.getProject();
    myTempFolderMapping = TempFolderMapping.create(info);
  }

  /**
   * Psalm analyzes the temp copy of the file, so it resolves {@code __DIR__} inside the temp folder.
   * An include of a file that exists in the project is then reported as missing.
   * This method drops such a message, and shows the project path in a message about a file that is really missing.
   */
  @Override
  protected void addMessage(QualityToolMessage message) {
    if (myTempFolderMapping != null && !message.isInternalError()) {
      Matcher matcher = MISSING_FILE_MESSAGE.matcher(message.getMessageText());
      if (matcher.find()) {
        String reportedPath = matcher.group(1);
        String originalPath = myTempFolderMapping.toOriginalPath(reportedPath);
        if (originalPath != null) {
          Path path = PhpPathUtils.tryParsePath(originalPath);
          if (path != null && Files.exists(path)) return;
          message.substitute(reportedPath, originalPath);
        }
      }
    }
    super.addMessage(message);
  }

  @Override
  protected XMLMessageHandler getXmlMessageHandler() {
    return new PsalmXmlMessageHandler();
  }

  @Override
  public int getMessageStart(@NotNull String line) {
    return line.indexOf(WARNING_MESSAGE_START);
  }

  @Override
  public int getMessageEnd(@NotNull String line) {
    return line.indexOf(WARNING_MESSAGE_END);
  }

  @Override
  protected void processMessage(InputSource source) throws SAXException, IOException {
    if (myFile != null) {
      super.processMessage(source);
    }
    else {
      PsalmXmlMessageHandler messageHandler = (PsalmXmlMessageHandler)getXmlMessageHandler();
      mySAXParser.parse(source, messageHandler);
      final List<ProblemDescription> data = myProject.getUserData(PSALM_ANNOTATOR_INFO);
      final ProblemDescription problemDescription =
        new ProblemDescription(messageHandler.getSeverity(), messageHandler.getLineNumber(), messageHandler.getColumn(),
                               messageHandler.getMessageText(), messageHandler.getFile());
      if (data != null) {
        data.add(problemDescription);
        myProject.putUserData(PSALM_ANNOTATOR_INFO, data);
      }
      else {
        myProject.putUserData(PSALM_ANNOTATOR_INFO, new ArrayList<>(singletonList(problemDescription)));
      }
    }
  }

  @Override
  protected @NonNls @Nullable String getMessagePrefix() {
    return "psalm";
  }

  @Override
  protected PsalmQualityToolType getQualityToolType() {
    return PsalmQualityToolType.INSTANCE;
  }

  /**
   * Maps a path in the temp folder to the path of the same file in the original location.
   * The temp copy keeps the path of the original file relative to {@code tempRoot}.
   * For a file in the project, {@code originalRoot} is the project base path.
   * For a file outside the project, it is the directory of the file.
   * The paths use {@code /} as the separator.
   */
  private record TempFolderMapping(@NotNull String tempRoot, @NotNull String originalRoot) {
    static @Nullable TempFolderMapping create(@NotNull QualityToolAnnotatorInfo<?> info) {
      VirtualFile originalFile = info.getOriginalFile();
      if (info.getTempFile() == null || originalFile == null) return null;

      String suffix = "/" + QualityToolAnnotator.getPathInTempFolder(info.getProject(), originalFile);
      String tempPath = info.getTempFilePath();
      String originalPath = toSystemIndependentName(originalFile.getPath());
      if (!tempPath.endsWith(suffix) || !originalPath.endsWith(suffix)) return null;

      String tempRoot = tempPath.substring(0, tempPath.length() - suffix.length());
      if (tempRoot.isEmpty()) return null;
      return new TempFolderMapping(tempRoot, originalPath.substring(0, originalPath.length() - suffix.length()));
    }

    @Nullable String toOriginalPath(@NotNull String path) {
      String systemIndependentPath = toSystemIndependentName(path);
      if (!FileUtil.isAncestor(tempRoot, systemIndependentPath, true)) return null;
      String relativePath = FileUtil.getRelativePath(tempRoot, systemIndependentPath, '/');
      return relativePath == null ? null : originalRoot + "/" + relativePath;
    }
  }

  private static class PsalmXmlMessageHandler extends XMLMessageHandler {
    String myFile;
    int myColumn = 0;

    @Override
    protected void parseTag(@NotNull String tagName, @NotNull Attributes attributes) {
      if (tagName.equals("file")) {
        myFile = attributes.getValue("name");
      }
      else {
        myLineNumber = parseLineNumber(attributes.getValue(LINE_NUMBER_ATTR));
        mySeverity = attributes.getValue(SEVERITY_ATTR).equals("error") ? ERROR : WARNING;
        myMessageBuf.append(attributes.getValue(MESSAGE_ATTR));
        myColumn = parseLineNumber(attributes.getValue(COLUMN_NUMBER_ATTR));
      }
    }

    @Override
    public int getColumn() {
      return myColumn - 1;
    }

    public String getFile() {
      return myFile;
    }
  }
}