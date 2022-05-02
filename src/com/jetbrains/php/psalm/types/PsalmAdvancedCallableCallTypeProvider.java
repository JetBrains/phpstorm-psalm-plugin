package com.jetbrains.php.psalm.types;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.PhpCharBasedTypeKey;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import com.jetbrains.php.psalm.lang.inspections.PsalmAdvanceCallableParamsInspection;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class PsalmAdvancedCallableCallTypeProvider extends PhpCharBasedTypeKey implements PhpTypeProvider4 {

  @Override
  public @Nullable PhpType getType(PsiElement element) {
    if (element instanceof FunctionReference && StringUtil.isEmpty(((FunctionReference)element).getName())) {
      if (element instanceof MethodReference) {
        return null;
      }
      return PhpType.from(((FunctionReference)element).getFirstPsiChild())
        .filterOut(t -> !(PhpType.isUnresolved(t) || PsalmAdvanceCallableParamsInspection.isParametrizedAdvancedCallable(t)), true)
        .map(this::sign);
    }
    return null;
  }

  @Override
  public @Nullable PhpType complete(String expression, Project project) {
    return PsalmAdvanceCallableParamsInspection.getAdvancedCallableTypes(project, PhpType.global(project, expression.substring(2))).stream()
      .map(ContainerUtil::getLastItem).filter(Objects::nonNull)
      .reduce(new PhpType(), PhpType::add);
  }

  @Override
  public Collection<? extends PhpNamedElement> getBySignature(String expression, Set<String> visited, int depth, Project project) {
    return null;
  }

  @Override
  public char getKey() {
    return 'ᤕ';
  }
}
