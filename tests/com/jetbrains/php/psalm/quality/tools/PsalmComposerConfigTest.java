package com.jetbrains.php.psalm.quality.tools;

import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileUtilRt;
import com.jetbrains.php.config.composer.QualityToolsComposerConfigTest;
import com.jetbrains.php.tools.quality.QualityToolConfigurationManager;
import com.jetbrains.php.tools.quality.psalm.PsalmConfiguration;
import com.jetbrains.php.tools.quality.psalm.PsalmConfigurationManager;
import org.jetbrains.annotations.NotNull;

public class PsalmComposerConfigTest extends QualityToolsComposerConfigTest {

  @Override
  protected QualityToolConfigurationManager<PsalmConfiguration> getQualityToolConfigurationManager() {
    return PsalmConfigurationManager.getInstance(getProject());
  }

  @NotNull
  @Override
  protected String getPath() {
    return FileUtilRt.toSystemIndependentName(myFixture.getTempDirPath()) + "/vendor/bin/psalm" + (SystemInfo.isWindows ? ".bat" : "");
  }

  @NotNull
  @Override
  protected String getPackageName() {
    return "vimeo/psalm";
  }
}
