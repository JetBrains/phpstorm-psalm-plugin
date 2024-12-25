package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.util.NlsSafe;
import com.intellij.util.xmlb.XmlSerializer;
import com.jetbrains.php.tools.quality.QualityToolConfigurationBaseManager;
import com.jetbrains.php.tools.quality.QualityToolType;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsalmConfigurationBaseManager extends QualityToolConfigurationBaseManager<PsalmConfiguration> {
  private static final @NonNls String PSALM_PATH = "PsalmPath";
  public static final @NlsSafe String PSALM = "Psalm";
  private static final @NonNls String ROOT_NAME = "Psalm_settings";

  @Override
  protected @NotNull QualityToolType<PsalmConfiguration> getQualityToolType() {
    return PsalmQualityToolType.INSTANCE;
  }

  @Override
  protected @NotNull String getOldStyleToolPathName() {
    return PSALM_PATH;
  }

  @Override
  protected @NotNull String getConfigurationRootName() {
    return ROOT_NAME;
  }

  @Override
  protected @Nullable PsalmConfiguration loadLocal(Element element) {
    return XmlSerializer.deserialize(element, PsalmConfiguration.class);
  }
}
