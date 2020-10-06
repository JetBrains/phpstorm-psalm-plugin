package com.jetbrains.php.psalm.types;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.ModificationTracker;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.util.indexing.FileBasedIndex;
import com.jetbrains.php.lang.psi.resolve.types.PhpMetaTypeMappingsTable;
import com.jetbrains.php.lang.psi.stubs.indexes.PhpMetaTableProvider;
import com.jetbrains.php.lang.psi.stubs.indexes.PhpMetaTableProviderImpl;
import com.jetbrains.php.lang.psi.stubs.indexes.PhpMetaTypeInferenceMappingIndex;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PsalmMetaTableProvider implements PhpMetaTableProvider {
  @Override
  public @NotNull Map<String, PhpMetaTypeMappingsTable> getTable(@NotNull Project project) {
    return CachedValuesManager.getManager(project).getCachedValue(project, () -> {
      return new CachedValueProvider.Result<>(PhpMetaTableProviderImpl.getMetaTable(project, PsalmTemplateIndex.KEY), getModificationTracker(project));
    });
  }

  @NotNull
  private static ModificationTracker getModificationTracker(@NotNull Project project) {
    return () -> FileBasedIndex.getInstance().getIndexModificationStamp(PsalmTemplateIndex.KEY, project);
  }
}
