package com.jetbrains.php.psalm;

import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import com.jetbrains.php.lang.PhpLangUtil;
import com.jetbrains.php.lang.psi.PhpNoReturnProvider;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class PsalmNoReturnProvider implements PhpNoReturnProvider {
  @Override
  public boolean isCustomNoReturn(@NotNull FunctionReference functionCall) {
    String name = StringUtil.toLowerCase(functionCall.getName());
    if (StringUtil.isEmpty(name)) return false;
    return mayBeCustomReturn(functionCall, name) && Arrays.stream(functionCall.multiResolve(false))
      .map(ResolveResult::getElement)
      .anyMatch(e -> e instanceof Function && containsNoReturnPhpDocType(((Function)e), StringUtil.toLowerCase(((Function)e).getName())));
  }

  private static boolean mayBeCustomReturn(@NotNull FunctionReference functionCall, String name) {
    Ref<Boolean> res = new Ref<>(false);
    FileBasedIndex.getInstance().processValues(PsalmNoReturnFunctionsIndex.KEY, name, null, (file, value) -> {
      res.set(true);
      return false;
    }, GlobalSearchScope.allScope(functionCall.getProject()));
    return res.get();
  }

  private static boolean containsNoReturnPhpDocType(@NotNull Function function, String name) {
    Ref<Boolean> res = new Ref<>(false);
    FileBasedIndex.getInstance().processValues(PsalmNoReturnFunctionsIndex.KEY, name, null, (file, value) -> {
      if (PhpLangUtil.equalsFunctionNames(function.getFQN(), value)) {
        res.set(true);
        return false;
      }
      return true;
    }, GlobalSearchScope.allScope(function.getProject()));
    return res.get();
  }
}
