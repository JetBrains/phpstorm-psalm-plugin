package com.jetbrains.php.psalm.lang;

import com.intellij.lang.parameterInfo.*;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IntPair;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.lang.PhpParameterInfoHandler;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.Statement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.psalm.lang.inspections.PsalmAdvanceCallableParamsInspection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class PsalmAdvancedClosureParameterInfoHandler implements ParameterInfoHandlerWithTabActionSupport<FunctionReference, List<Pair<String, PhpType>>, PhpPsiElement> {
  private String text;

  @Override
  public @Nullable FunctionReference findElementForParameterInfo(@NotNull CreateParameterInfoContext context) {
    FunctionReference reference = ObjectUtils.tryCast(PhpParameterInfoHandler.findAnchorForParameterInfo(context), FunctionReference.class);
    if (reference != null && StringUtil.isEmpty(reference.getName())) {
      List<Pair<String, PhpType>> types = PsalmAdvanceCallableParamsInspection.getAdvancedCallableParametersTypes(reference);
      if (!types.isEmpty()) {
        context.setItemsToShow(new Object[]{types});
        return reference;
      }
    }
    return null;
  }

  @Override
  public void showParameterInfo(@NotNull FunctionReference element, @NotNull CreateParameterInfoContext context) {
    context.showHint(element, element.getTextRange().getStartOffset(), this);
  }

  @Override
  public @Nullable FunctionReference findElementForUpdatingParameterInfo(@NotNull UpdateParameterInfoContext context) {
    return ObjectUtils.tryCast(PhpParameterInfoHandler.findAnchorForParameterInfo(context), FunctionReference.class);
  }

  @Override
  public void updateParameterInfo(@NotNull FunctionReference reference, @NotNull UpdateParameterInfoContext context) {
    context.setCurrentParameter(PhpParameterInfoHandler.getCurrentParameterIndex(reference, PhpParameterInfoHandler.getCurrentOffset(context), getActualParameterDelimiterType()));
  }

  @Override
  public void updateUI(List<Pair<String, PhpType>> p, @NotNull ParameterInfoUIContext context) {
    if (p == null) {
      context.setUIComponentEnabled(false);
      return;
    }
    int currentParameter = context.getCurrentParameterIndex();
    if (currentParameter < 0) currentParameter = 0;
    StringBuilder buffer = new StringBuilder();
    IntPair highlightRange =
      PhpParameterInfoHandler.appendParameterInfo(context, buffer, new IntPair(-1, -1), currentParameter, p,
                                                  PsalmAdvancedClosureParameterInfoHandler::getPresentation);
    context.setupUIComponentPresentation(
      text = buffer.toString(),
      highlightRange.first,
      highlightRange.second,
      false,
      false,
      false,
      context.getDefaultParameterColor());
  }

  private static String getPresentation(Pair<String, PhpType> p) {
    String name = p.getFirst();
    return name.isEmpty() ? p.getSecond().toString() : name + ": " + p.getSecond().toString();
  }

  @Override
  public PhpPsiElement @NotNull [] getActualParameters(@NotNull FunctionReference o) {
    final PsiElement[] parameters = o.getParameters();
    return Arrays.copyOf(parameters, parameters.length, PhpPsiElement[].class);
  }

  @Override
  public @NotNull IElementType getActualParameterDelimiterType() {
    return PhpTokenTypes.opCOMMA;
  }

  @Override
  public @NotNull IElementType getActualParametersRBraceType() {
    return PhpTokenTypes.chRPAREN;
  }

  @Override
  public @NotNull Set<Class<?>> getArgumentListAllowedParentClasses() {
    return ContainerUtil.newHashSet(PhpPsiElement.class);
  }

  @Override
  public @NotNull Set<? extends Class<?>> getArgListStopSearchClasses() {
    return ContainerUtil.newHashSet(Statement.class);
  }

  @Override
  public @NotNull Class<FunctionReference> getArgumentListClass() {
    return FunctionReference.class;
  }

  @TestOnly
  public String getText() {
    return text;
  }

}
