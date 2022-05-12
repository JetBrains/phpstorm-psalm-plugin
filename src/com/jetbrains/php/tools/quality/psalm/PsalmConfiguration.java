package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.util.xmlb.annotations.Transient;
import com.jetbrains.php.tools.quality.QualityToolConfiguration;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.jetbrains.php.tools.quality.psalm.PsalmConfigurationManager.DEFAULT_MAX_MESSAGES_PER_FILE;


/**
 * Stores configuration needed to run Psalm in selected environment.
 */
public class PsalmConfiguration implements QualityToolConfiguration {
  private String myPsalmPath = "";
  private int myMaxMessagesPerFile = DEFAULT_MAX_MESSAGES_PER_FILE;
  private int myTimeoutMs = 30000;

  @Override
  @Transient
  public String getToolPath() {
    return myPsalmPath;
  }

  @Override
  public void setToolPath(String toolPath) {
    myPsalmPath = toolPath;
  }

  @SuppressWarnings("UnusedDeclaration")
  @Nullable
  @Attribute("tool_path")
  public String getSerializedToolPath() {
    return serialize(myPsalmPath);
  }

  @SuppressWarnings("UnusedDeclaration")
  public void setSerializedToolPath(@Nullable String configurationFilePath) {
    myPsalmPath = deserialize(configurationFilePath);
  }

  @Override
  @Attribute("max_messages_per_file")
  public int getMaxMessagesPerFile() {
    return myMaxMessagesPerFile;
  }

  @Override
  @Attribute("timeout")
  public int getTimeout() {
    return myTimeoutMs;
  }

  @Override
  public void setTimeout(int timeout) {
    myTimeoutMs = timeout;
  }

  @Override
  @NotNull
  public @Nls String getId() {
    return PsalmBundle.message("local");
  }

  @Override
  @Nullable
  public String getInterpreterId() {
    return null;
  }

  @Override
  public PsalmConfiguration clone() {
    PsalmConfiguration settings = new PsalmConfiguration();
    clone(settings);
    return settings;
  }

  public PsalmConfiguration clone(@NotNull PsalmConfiguration settings) {
    settings.myPsalmPath = myPsalmPath;
    settings.myMaxMessagesPerFile = myMaxMessagesPerFile;
    settings.myTimeoutMs = myTimeoutMs;
    return settings;
  }

  @Override
  public int compareTo(@NotNull QualityToolConfiguration o) {
    if (!(o instanceof PsalmConfiguration)) {
      return 1;
    }

    if (StringUtil.equals(getPresentableName(null), PsalmBundle.message("label.system.php"))) {
      return -1;
    }
    else if (StringUtil.equals(o.getPresentableName(null), PsalmBundle.message("label.system.php"))) {
      return 1;
    }
    return StringUtil.compare(getPresentableName(null), o.getPresentableName(null), false);
  }
}
