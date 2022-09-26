package com.jetbrains.php.psalm.lang;

import com.intellij.openapi.application.QueryExecutorBase;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.QuerySearchRequest;
import com.intellij.psi.search.UsageSearchContext;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.Processor;
import com.jetbrains.php.lang.psi.elements.Field;
import org.jetbrains.annotations.NotNull;

public class PsalmExtendedClassConstantReferenceSearcher extends QueryExecutorBase<PsiReference, ReferencesSearch.SearchParameters> {

  public PsalmExtendedClassConstantReferenceSearcher() {
    super(true);
  }
  @Override
  public void processQuery(ReferencesSearch.@NotNull SearchParameters queryParameters, @NotNull Processor<? super PsiReference> consumer) {
    PsiElement refElement = queryParameters.getElementToSearch();
    if (refElement instanceof Field field && field.isConstant()) {
      String name = field.getName();
      for (int i = 1; i < name.length(); i++) {
        // "::*" won't be processed correctly as a word, so special searcher for this case is implemented in PsalmExtendedClassConstantWildcardReferenceSearcher
        String wildcardName = "::" + name.substring(0, i) + "*";
        queryParameters.getOptimizer().searchWord(wildcardName, queryParameters.getEffectiveSearchScope(), UsageSearchContext.IN_COMMENTS, true, refElement);
      }
    }
  }
}
