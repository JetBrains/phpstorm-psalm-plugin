package com.jetbrains.php.psalm;

import com.intellij.openapi.util.Ref;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import com.jetbrains.php.lang.psi.PhpNoReturnProvider;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class PsalmNoReturnProvider implements PhpNoReturnProvider {
  @Override
  public boolean isCustomNoReturn(@NotNull FunctionReference functionCall) {
    return Arrays.stream(functionCall.multiResolve(false))
      .map(ResolveResult::getElement)
      .anyMatch(e -> e instanceof Function && containsNoReturnPhpDocType(((Function)e)));
  }

  private static boolean containsNoReturnPhpDocType(@NotNull Function function) {
    Ref<Boolean> res = new Ref<>(false);
    FileBasedIndex.getInstance().processValues(PsalmNoReturnFunctionsIndex.KEY, function.getFQN(), null, (file, value) -> {
      res.set(true);
      return false;
    }, GlobalSearchScope.allScope(function.getProject()));
    return res.get();
  }
}
