package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.jetbrains.php.tools.quality.QualityToolsOptionsConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "PsalmOptionsConfiguration", storages = @Storage("php.xml"))
public class PsalmOptionsConfiguration extends QualityToolsOptionsConfiguration implements PersistentStateComponent<PsalmOptionsConfiguration> {

  @NlsSafe private String config = "";
  private boolean showInfo = false;
  private boolean findUnusedCode = false;
  private boolean findUnusedSuppress = false;

  public String getConfig() {
    return config;
  }

  public void setConfig(String config) {
    this.config = config;
  }

  public boolean isShowInfo() {
    return showInfo;
  }

  public void setShowInfo(boolean showInfo) {
    this.showInfo = showInfo;
  }

  public boolean isFindUnusedCode() {
    return findUnusedCode;
  }

  public void setFindUnusedCode(boolean findUnusedCode) {
    this.findUnusedCode = findUnusedCode;
  }

  public boolean isFindUnusedSuppress() {
    return findUnusedSuppress;
  }

  public void setFindUnusedSuppress(boolean findUnusedSuppress) {
    this.findUnusedSuppress = findUnusedSuppress;
  }

  @Nullable
  @Override
  public PsalmOptionsConfiguration getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull PsalmOptionsConfiguration state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  public static PsalmOptionsConfiguration getInstance(Project project) {
    return project.getService(PsalmOptionsConfiguration.class);
  }
}
