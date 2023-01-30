package com.jetbrains.php.psalm;

import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiFile;
import com.intellij.util.indexing.*;
import com.intellij.util.io.*;
import com.jetbrains.php.lang.PhpLangUtil;
import com.jetbrains.php.lang.documentation.phpdoc.PhpDocUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.inspections.parameterCountMismatch.PhpFuncGetArgUsageProvider;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.stubs.indexes.PhpConstantNameIndex;
import com.jetbrains.php.psalm.types.PsalmDocTagTypeProvider;
import com.jetbrains.php.psalm.types.PsalmExtendedStringDocTypeProvider;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PsalmNoReturnFunctionsIndex extends FileBasedIndexExtension<String, String> {
  @NonNls public static final ID<String, String> KEY = ID.create("psalm.no.return");

  @Override
  public @NotNull DataExternalizer<String> getValueExternalizer() {
    return EnumeratorStringDescriptor.INSTANCE;
  }

  @NotNull
  @Override
  public FileBasedIndex.InputFilter getInputFilter() {
    return PhpConstantNameIndex.PHP_INPUT_FILTER;
  }

  @NotNull
  @Override
  public ID<String, String> getName() {
    return KEY;
  }

  @NotNull
  @Override
  public KeyDescriptor<String> getKeyDescriptor() {
    return EnumeratorStringDescriptor.INSTANCE;
  }

  @Override
  public boolean dependsOnFileContent() {
    return true;
  }

  @Override
  public int getVersion() {
    return 2;
  }

  @Override
  public @NotNull DataIndexer<String, String, FileContent> getIndexer() {
    return inputData -> {
      PsiFile file = inputData.getPsiFile();
      if (!(file instanceof PhpFile)) {
        return Collections.emptyMap();
      }
      Map<String, String> map = new HashMap<>();
      ((PhpFile)file).getTopLevelDefs().values().stream()
        .flatMap(PhpFuncGetArgUsageProvider::getFunctions)
        .filter(PsalmNoReturnFunctionsIndex::hasPsalmNoReturnTag)
        .forEach(e -> map.put(StringUtil.toLowerCase(e.getName()), e.getFQN()));
      return map;
    };
  }

  private static boolean hasPsalmNoReturnTag(Function f) {
    PhpDocComment comment = f.getDocComment();
    if (comment == null) return false;
    Ref<Boolean> res = new Ref<>(false);
    PhpDocUtil.processTagElementsByNames(comment, tag -> {
      PhpDocType docType = PhpPsiUtil.getChildByCondition(tag, PhpDocType.INSTANCEOF);
      res.set(res.get() || docType != null && PsalmExtendedStringDocTypeProvider.NO_RETURN_TYPES.containsKey(docType.getText()));
    }, PhpDocUtil.RETURN_TAG, PsalmDocTagTypeProvider.RETURN);
    return res.get();
  }
}
