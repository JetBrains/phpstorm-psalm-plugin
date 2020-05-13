package com.jetbrains.php.tools.quality.psalm;

import com.jetbrains.php.tools.quality.QualityToolAnnotatorInfo;
import com.jetbrains.php.tools.quality.QualityToolType;
import com.jetbrains.php.tools.quality.QualityToolXmlMessageProcessor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import static com.jetbrains.php.tools.quality.QualityToolMessage.Severity.ERROR;
import static com.jetbrains.php.tools.quality.QualityToolMessage.Severity.WARNING;

public class PsalmMessageProcessor extends QualityToolXmlMessageProcessor {
  @NonNls private final static String WARNING_MESSAGE_START = "<error";
  @NonNls private final static String WARNING_MESSAGE_END = "/>";
  @NonNls private final static String LINE_NUMBER_ATTR = "line";
  @NonNls private final static String COLUMN_NUMBER_ATTR = "column";
  private final static String MESSAGE_ATTR = "message";
  @NonNls private final static String SEVERITY_ATTR = "severity";

  protected PsalmMessageProcessor(QualityToolAnnotatorInfo info) {
    super(info);
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

  @NonNls
  @Nullable
  @Override
  protected String getMessagePrefix() {
    return "psalm";
  }

  @Override
  protected QualityToolType<PsalmConfiguration> getQualityToolType() {
    return PsalmQualityToolType.INSTANCE;
  }

  @Override
  public boolean processStdErrMessages() {
    return false;
  }

  private static class PsalmXmlMessageHandler extends XMLMessageHandler {
    int myColumn = 0;

    @Override
    protected void parseTag(@NotNull String tagName, @NotNull Attributes attributes) {
      myLineNumber = parseLineNumber(attributes.getValue(LINE_NUMBER_ATTR));
      mySeverity = attributes.getValue(SEVERITY_ATTR) == "error" ? ERROR: WARNING;
      myMessageBuf.append(attributes.getValue(MESSAGE_ATTR));
      myColumn = parseLineNumber(attributes.getValue(COLUMN_NUMBER_ATTR));
    }

    @Override
    protected int getColumn() {
      return myColumn;
    }
  }
}