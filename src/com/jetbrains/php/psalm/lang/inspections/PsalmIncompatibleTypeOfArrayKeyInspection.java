package com.jetbrains.php.psalm.lang.inspections;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.util.ObjectUtils;
import com.jetbrains.php.lang.inspections.PhpInspection;
import com.jetbrains.php.lang.psi.elements.ArrayAccessExpression;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import com.jetbrains.php.lang.psi.elements.Variable;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor;
import com.jetbrains.php.psalm.types.PsalmDummyArrayKeyTypeProvider;
import com.jetbrains.php.tools.quality.psalm.PsalmBundle;
import org.jetbrains.annotations.NotNull;

public class PsalmIncompatibleTypeOfArrayKeyInspection extends PhpInspection {
  private static final String PSALM_KEY_TYPE_SIGN = "#" + PsalmDummyArrayKeyTypeProvider.KEY;
  private static final PhpType INT_STRING = PhpType.or(PhpType.INT, PhpType.STRING);

  @Override
  public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
    return new PhpElementVisitor() {
      @Override
      public void visitPhpArrayAccessExpression(ArrayAccessExpression expression) {
        super.visitPhpArrayAccessExpression(expression);
        if (expression.getIndex() == null) return;
        Variable variable = ObjectUtils.tryCast(expression.getValue(), Variable.class);
        if (variable == null) return;
        PhpType keyType = getDeclaredKeyType(variable);
        if (keyType.isAmbiguous() || !PhpType.intersects(INT_STRING, keyType)) return;
        var typedIndexValue = ObjectUtils.tryCast(expression.getIndex().getValue(), PhpTypedElement.class);
        if (typedIndexValue == null) return;
        PhpType indexValueType = typedIndexValue.getType().global(expression.getProject());
        if (indexValueType.isAmbiguous()) return;
        if (PhpType.intersects(keyType, indexValueType)) return;
        if (isStringLiteralNumber(typedIndexValue) && PhpType.intersects(PhpType.INT, keyType)) return;
        holder.registerProblem(typedIndexValue, PsalmBundle.message("incompatible.type.of.array.key.expecting", keyType.toString()));
      }
    };
  }

  private static boolean isStringLiteralNumber(@NotNull PsiElement element) {
    StringLiteralExpression stringLiteral = ObjectUtils.tryCast(element, StringLiteralExpression.class);
    return stringLiteral != null && StringUtil.isNotNegativeNumber(stringLiteral.getContents());
  }

  private static PhpType getDeclaredKeyType(@NotNull Variable variable) {
    return variable.getType().getTypes().stream()
      .filter(t -> t.startsWith(PSALM_KEY_TYPE_SIGN))
      .map(t -> t.substring(PSALM_KEY_TYPE_SIGN.length()))
      .reduce(new PhpType(), PhpType::add, PhpType::or);
  }
}
