package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.jetbrains.php.tools.quality.QualityToolProjectConfiguration;
import com.jetbrains.php.tools.quality.QualityToolType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "PsalmProjectConfiguration", storages = @Storage(StoragePathMacros.WORKSPACE_FILE))
public class PsalmProjectConfiguration extends QualityToolProjectConfiguration<PsalmConfiguration>
  implements PersistentStateComponent<PsalmProjectConfiguration> {

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
  public PsalmProjectConfiguration getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull PsalmProjectConfiguration state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  @Override
  protected QualityToolType<PsalmConfiguration> getQualityToolType() {
    return PsalmQualityToolType.INSTANCE;
  }

  public static PsalmProjectConfiguration getInstance(Project project) {
    return project.getService(PsalmProjectConfiguration.class);
  }
}
