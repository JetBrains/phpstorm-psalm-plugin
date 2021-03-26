package com.jetbrains.php.tools.quality.psalm;

import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.project.Project;
import com.jetbrains.php.tools.quality.QualityToolBlackList;

@State(name = "PsalmBlackList", storages = @Storage(StoragePathMacros.WORKSPACE_FILE))
public class PsalmBlackList extends QualityToolBlackList {

  public static PsalmBlackList getInstance(Project project) {
    return project.getService(PsalmBlackList.class);
  }
}
