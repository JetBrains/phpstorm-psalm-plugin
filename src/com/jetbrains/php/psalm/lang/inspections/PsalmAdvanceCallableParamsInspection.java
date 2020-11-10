package com.jetbrains.php.psalm.lang.inspections;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.PhpIndexImpl;
import com.jetbrains.php.lang.inspections.PhpInspection;
import com.jetbrains.php.lang.inspections.type.PhpParamsInspection;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor;
import com.jetbrains.php.psalm.types.PsalmAdvancedCallableTypeProvider;
import com.jetbrains.php.tools.quality.psalm.PsalmBundle;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

public class PsalmAdvanceCallableParamsInspection extends PhpInspection {
  @Override
  public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
    return new PhpElementVisitor() {
      @Override
      public void visitPhpFunctionCall(FunctionReference reference) {
        if (StringUtil.isEmpty(reference.getName())) {
          PsiElement[] parameters = reference.getParameters();
          if (parameters.length == 0) return;
          List<PhpType> parameterTypes = getAdvancedCallableParametersTypes(reference);
          if (parameterTypes != null) {
            for (int i = 0; i < Math.min(parameters.length, parameterTypes.size()); i++) {
              PhpType callType = new PhpType().add(parameters[i]);
              if (PhpParamsInspection.isCallTypeConvertibleFromDeclaredType(callType, parameterTypes.get(i), PhpIndex.getInstance(
                holder.getProject()))) continue;
              String message = PsalmBundle
                .message("parameter.type.is.not.compatible.with.declaration", callType.toStringResolved(), parameterTypes.get(i).toStringResolved());
              holder.registerProblem(parameters[i], message);
            }
          }
        }
      }

      private List<PhpType> getAdvancedCallableParametersTypes(FunctionReference reference) {
        Ref<List<PhpType>> paramTypes = new Ref<>();
        Function<String, PhpType> paramsTypeSearcher = s -> {
          if (PsalmAdvancedCallableTypeProvider.isSigned(s)) {
            paramTypes.set(PsalmAdvancedCallableTypeProvider.getParametersTypes(s));
          }
          return null;
        };
        ((PhpIndexImpl)PhpIndex.getInstance(holder.getProject())).completeType(holder.getProject(), new PhpType().add(reference.getFirstPsiChild()), new HashSet<>(), paramsTypeSearcher);

        return paramTypes.get();
      }
    };
  }
}
