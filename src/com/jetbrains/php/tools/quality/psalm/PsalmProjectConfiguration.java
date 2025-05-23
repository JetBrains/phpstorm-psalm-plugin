package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.jetbrains.php.tools.quality.QualityToolProjectConfiguration;
import com.jetbrains.php.tools.quality.QualityToolType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "PsalmProjectConfiguration", storages = @Storage(StoragePathMacros.WORKSPACE_FILE))
public class PsalmProjectConfiguration extends QualityToolProjectConfiguration<PsalmConfiguration>
  implements PersistentStateComponent<PsalmProjectConfiguration> {

  @Override
  public @Nullable PsalmProjectConfiguration getState() {
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
