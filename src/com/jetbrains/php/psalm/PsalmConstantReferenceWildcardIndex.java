package com.jetbrains.php.psalm;

import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.util.indexing.ID;
import com.intellij.util.io.DataExternalizer;
import com.intellij.util.text.StringSearcher;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.stubs.indexes.PhpConstantNameIndex;
import com.jetbrains.php.lang.psi.stubs.indexes.PhpFlatCollectionIndexBase;
import com.jetbrains.php.lang.psi.stubs.indexes.PhpInvokeCallsOffsetsIndex;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class PsalmConstantReferenceWildcardIndex extends PhpFlatCollectionIndexBase<IntList> {

  @NonNls public static final ID<Boolean, IntList> KEY = ID.create("psalm.constant.reference.wildcard");

  @Override
  public @NotNull ID<Boolean, IntList> getName() {
    return KEY;
  }

  @Override
  protected IntList collectValues(@NotNull PhpFile file) {
    CharSequence text = file.getText();
    return new IntArrayList(new StringSearcher("::*", true, true).findAllOccurrences(text));
  }


  @Override
  public @NotNull DataExternalizer<IntList> getValueExternalizer() {
    return PhpInvokeCallsOffsetsIndex.IntArrayExternalizer.INSTANCE;
  }

  @Override
  public int getVersion() {
    return 0;
  }

  @Override
  public FileBasedIndex.@NotNull InputFilter getInputFilter() {
    return PhpConstantNameIndex.PHP_INPUT_FILTER;
  }
}
