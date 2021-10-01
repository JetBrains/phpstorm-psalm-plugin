package com.jetbrains.php.psalm.types;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.PhpWorkaroundUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpKeyTypeProvider;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

import static com.jetbrains.php.lang.psi.PhpPsiUtil.isOfType;

public class PsalmParamTypeProvider implements PhpTypeProvider4 {

  @Override
  public char getKey() {
    return 'ᢒ';
  }

  @Override
  public @Nullable PhpType getType(PsiElement element) {
    return element instanceof PhpDocType ? parsePsalmType((PhpDocType)element) : null;
  }

  private static @Nullable PhpType parsePsalmType(@NotNull PhpDocType docType) {
    if (!PhpWorkaroundUtil.isGenericArray(docType)) return null;
    return PhpWorkaroundUtil.valueDocTypes(docType)
      .map(PhpTypedElement::getType)
      .reduce(new PhpType(), PhpType::add)
      .map(t -> PhpKeyTypeProvider.isArrayKeySignature(t) ? signWithSameKey(t) : PhpType.pluralise(t, 1));
  }

  @NotNull
  private static String signWithSameKey(@NotNull String t) {
    return t.substring(0, 2) + t;
  }

  @Override
  public @Nullable PhpType complete(String expression, Project project) {
    return null;
  }

  @Override
  public Collection<? extends PhpNamedElement> getBySignature(String expression, Set<String> visited, int depth, Project project) {
    return null;
  }
}
