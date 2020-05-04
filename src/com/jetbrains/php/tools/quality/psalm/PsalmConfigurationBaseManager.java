package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.util.xmlb.XmlSerializer;
import com.jetbrains.php.tools.quality.QualityToolConfigurationBaseManager;
import com.jetbrains.php.tools.quality.QualityToolType;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsalmConfigurationBaseManager extends QualityToolConfigurationBaseManager<PsalmConfiguration> {
  @NonNls private static final String PSALM_PATH = "PsalmPath";
  @NonNls public static final String PSALM = "Psalm";
  @NonNls private static final String ROOT_NAME = "Psalm_settings";

  @Override
  protected @NotNull QualityToolType<PsalmConfiguration> getQualityToolType() {
    return PsalmQualityToolType.INSTANCE;
  }

  @NotNull
  @Override
  protected String getOldStyleToolPathName() {
    return PSALM_PATH;
  }

  @NotNull
  @Override
  protected String getConfigurationRootName() {
    return ROOT_NAME;
  }

  @Override
  @Nullable
  protected PsalmConfiguration loadLocal(Element element) {
    return XmlSerializer.deserialize(element, PsalmConfiguration.class);
  }

  public static PsalmConfigurationBaseManager getInstance() {
    return ServiceManager.getService(PsalmConfigurationBaseManager.class);
  }
}
