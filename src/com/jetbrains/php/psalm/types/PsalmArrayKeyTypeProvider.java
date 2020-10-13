package com.jetbrains.php.psalm.types;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpKeyTypeProvider;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

public class PsalmArrayKeyTypeProvider implements PhpKeyTypeProvider {
  @Override
  public char getKey() {
    return PsalmDummyArrayKeyTypeProvider.KEY;
  }

  @Override
  public Collection<? extends PhpNamedElement> getBySignature(String expression, Set<String> visited, int depth, Project project) {
    return null;
  }

  @Override
  public @NotNull PhpType getType(PsiElement array) {
    return new PhpType().add(array).filterOut(s -> !PsalmDummyArrayKeyTypeProvider.isSigned(s));
  }

  @Override
  public @NotNull PhpType complete(String expression, Project project) {
    return new PhpType().add(expression.substring(2));
  }
}
