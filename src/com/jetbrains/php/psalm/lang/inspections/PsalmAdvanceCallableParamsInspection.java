package com.jetbrains.php.psalm.lang.inspections;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.inspections.PhpInspection;
import com.jetbrains.php.lang.inspections.type.PhpParamsInspection;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.jetbrains.php.lang.psi.elements.impl.PhpExpressionImpl;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor;
import com.jetbrains.php.psalm.types.PsalmAdvancedCallableTypeProvider;
import com.jetbrains.php.tools.quality.psalm.PsalmBundle;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PsalmAdvanceCallableParamsInspection extends PhpInspection {
  @Override
  public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
    return new PhpElementVisitor() {
      @Override
      public void visitPhpFunctionCall(FunctionReference reference) {
        if (StringUtil.isEmpty(reference.getName())) {
          PsiElement[] parameters = reference.getParameters();
          if (parameters.length == 0) return;
          List<Pair<String, PhpType>> parameterTypes = getAdvancedCallableParametersTypes(reference);
          if (parameterTypes != null) {
            for (int i = 0; i < Math.min(parameters.length, parameterTypes.size()); i++) {
              PhpType callType = new PhpType().add(parameters[i]).global(holder.getProject());
              PhpType parameterType = parameterTypes.get(i).getSecond().global(holder.getProject());
              if (PhpParamsInspection.isCallTypeConvertibleFromDeclaredType(callType, parameterType, PhpIndex.getInstance(holder.getProject()))) continue;
              String message = PsalmBundle
                .message("parameter.type.is.not.compatible.with.declaration", callType.toStringResolved(), parameterType.toStringResolved());
              holder.registerProblem(parameters[i], message);
            }
          }
        }
      }
    };
  }

  public static List<Pair<String, PhpType>> getAdvancedCallableParametersTypes(FunctionReference reference) {
    List<Pair<String, PhpType>> types = ContainerUtil.getFirstItem(getAdvancedCallableTypes(reference.getProject(), new PhpType().add(reference.getFirstPsiChild())));
    return types == null ? Collections.emptyList() : types.subList(0, types.size() - 1);
  }

  @NotNull
  public static List<List<Pair<String, PhpType>>> getAdvancedCallableTypes(Project project, PhpType type) {
    return type.global(project).getTypesWithParametrisedParts().stream()
      .filter(PsalmAdvanceCallableParamsInspection::isParametrizedAdvancedCallable)
      .map(PhpType::getParametrizedParts)
      .map(t -> ContainerUtil.map(t, PsalmAdvanceCallableParamsInspection::getType))
      .filter(t -> !t.isEmpty())
      .collect(Collectors.toList());
  }

  public static boolean isParametrizedAdvancedCallable(String t) {
    return PhpType.hasParameterizedPart(t) &&
           PsalmAdvancedCallableTypeProvider.ADVANCED_CALLABLES.contains(PhpType.removeParametrisedType(t));
  }

  @NotNull
  private static Pair<String, PhpType> getType(String serializedType) {
    String name = "";
    if (serializedType.startsWith(PhpExpressionImpl.CALLABLE_PARAMS_TYPES_SEPARATOR)) {
      int nameEnd = serializedType.indexOf(PhpExpressionImpl.CALLABLE_PARAMS_TYPES_SEPARATOR, 1);
      if (nameEnd >= 0) {
        name = serializedType.substring(1, nameEnd);
        serializedType = serializedType.substring(nameEnd + 1);
      }
    }
    PhpType type = new PhpType();
    StringUtil.split(serializedType, PhpExpressionImpl.CALLABLE_PARAMS_TYPES_SEPARATOR).forEach(type::add);
    return Pair.create(name, type);
  }
}
