package com.jetbrains.php.psalm.types;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.PhpIndexImpl;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.Variable;
import com.jetbrains.php.lang.psi.resolve.types.PhpCharBasedTypeKey;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeSignatureKey;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class PsalmAdvancedCallableCallTypeProvider extends PhpCharBasedTypeKey implements PhpTypeProvider4 {

  private static final @Nullable Function<String, PhpType> SUPPLIER =
    s -> {
      if (PsalmAdvancedCallableTypeProvider.isSigned(s)) {
        return new PhpType().add(PsalmAdvancedCallableTypeProvider.getReturnType(s));
      }
      return ContainerUtil.exists(PhpTypeSignatureKey.values(), key -> key.isSigned(s)) ? null : PhpType.EMPTY;
    };

  @Override
  public @Nullable PhpType getType(PsiElement element) {
    if (element instanceof FunctionReference && StringUtil.isEmpty(((FunctionReference)element).getName())) {
      if (element instanceof MethodReference) {
        return null;
      }
      Variable variable = ObjectUtils.tryCast(((FunctionReference)element).getFirstPsiChild(), Variable.class);
      if (variable != null) {
        return variable.getType().filterOut(t -> !PhpType.isUnresolved(t)).map(this::sign);
      }
    }
    return null;
  }

  @Override
  public @Nullable PhpType complete(String expression, Project project) {
    return ((PhpIndexImpl)PhpIndex.getInstance(project)).completeType(project, new PhpType().add(expression.substring(2)).filterOut(t -> !PhpType.isUnresolved(t)), new HashSet<>(), SUPPLIER);
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
