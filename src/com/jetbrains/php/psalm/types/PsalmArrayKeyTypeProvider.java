package com.jetbrains.php.psalm.types;

import com.intellij.openapi.project.Project;
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
    return PsalmDummyArrayKeyTypeProvider.KEY.getKey();
  }

  @Override
  public Collection<? extends PhpNamedElement> getBySignature(String expression, Set<String> visited, int depth, Project project) {
    return null;
  }

  @Override
  public @NotNull PhpType getType(PsiElement array) {
    return new PhpType().add(array).filterOut(s -> !PsalmDummyArrayKeyTypeProvider.KEY.signed(s));
  }

  @Override
  public @NotNull PhpType complete(String expression, Project project) {
    int dot = expression.lastIndexOf('.');
    if (dot >= 0) {
      String classReferenceType = expression.substring(dot + 1, expression.length());
      if (!PhpType.ITERABLE.isConvertibleFrom(project, PhpType.from(classReferenceType))) {
        return PhpType.EMPTY;
      }
      expression = expression.substring(0, dot);
    }
    return new PhpType().add(expression.substring(2));
  }
}
