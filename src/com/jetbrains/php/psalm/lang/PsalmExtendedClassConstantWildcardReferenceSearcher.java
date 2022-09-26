package com.jetbrains.php.psalm.lang;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.Processor;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocRef;
import com.jetbrains.php.lang.findUsages.PhpMethodReferenceAtOffsetFromFlatIndexSearcher;
import com.jetbrains.php.lang.findUsages.PhpReferenceAtOffsetSearcher;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.psalm.PsalmConstantReferenceWildcardIndex;
import it.unimi.dsi.fastutil.ints.IntList;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PsalmExtendedClassConstantWildcardReferenceSearcher extends PhpReferenceAtOffsetSearcher<Field> {
  @Override
  public void processQuery(ReferencesSearch.@NotNull SearchParameters queryParameters, @NotNull Processor<? super PsiReference> consumer) {
    if (queryParameters.getElementToSearch() instanceof Field field && field.isConstant()) {
      process(queryParameters, consumer, field);
    }
  }

  @Override
  protected boolean processReferences(int offset, Field elementToSearch, Processor<? super PsiReference> consumer, PsiFile psiFile) {
    PhpDocRef docRef = PhpPsiUtil.getParentOfClass(psiFile.findElementAt(offset), false, PhpDocRef.class);
    if (docRef == null) return true;
    for (PsiReference reference : docRef.getReferences()) {
      if (reference.isReferenceTo(elementToSearch) && !consumer.process(reference)) {
        return false;
      }
    }
    return true;
  }

  @Override
  protected @NotNull Map<VirtualFile, IntList> getMethodReferenceOffsetCandidates(@NotNull GlobalSearchScope scope) {
    return PhpMethodReferenceAtOffsetFromFlatIndexSearcher.getFileOffsets(scope, PsalmConstantReferenceWildcardIndex.KEY);
  }
}
