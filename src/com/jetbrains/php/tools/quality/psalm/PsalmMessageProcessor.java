package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.project.Project;
import com.jetbrains.php.tools.quality.QualityToolAnnotatorInfo;
import com.jetbrains.php.tools.quality.QualityToolXmlMessageProcessor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
  final Project myProject;

  protected PsalmMessageProcessor(QualityToolAnnotatorInfo<?> info) {
    super(info);
    myProject = info.getProject();
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