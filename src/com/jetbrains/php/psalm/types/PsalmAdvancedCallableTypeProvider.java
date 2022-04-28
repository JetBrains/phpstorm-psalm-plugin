package com.jetbrains.php.psalm.types;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.lang.PhpLangUtil;
import com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import com.jetbrains.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpUse;
import com.jetbrains.php.lang.psi.resolve.types.PhpCharBasedTypeKey;
import com.jetbrains.php.lang.psi.resolve.types.PhpParameterBasedTypeProvider;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class PsalmAdvancedCallableTypeProvider extends PhpCharBasedTypeKey implements PhpTypeProvider4 {

  public static final char KEY = '\u1913';
  private static final String TYPES_SEPARATOR = String.valueOf(KEY);
  private static final String PARAMETERS_SEPARATOR = ",";
  private static final Collection<String> ADVANCED_CALLABLES = Set.of(PhpType._CLOSURE, PhpType._CALLABLE);

  @Override
  public @Nullable PhpType getType(PsiElement element) {
    if (element instanceof PhpDocType && isAdvancedCallable(element)) {
      String fqn = resolveFQN(((PhpDocType)element));
      if (!ADVANCED_CALLABLES.contains(fqn)) {
        return null;
      }
      String returnType = getSerializedDocTypes(element);
      String parameterTypes = PhpPsiUtil.getChildren(element, PhpDocParamTag.class::isInstance).stream()
        .map(PsalmAdvancedCallableTypeProvider::getSerializedDocTypes)
        .collect(Collectors.joining(PARAMETERS_SEPARATOR));
      return new PhpType().add(sign(PhpParameterBasedTypeProvider.wrapTypes(Arrays.asList(fqn, parameterTypes, returnType))));
    }
    return null;
  }

  private static String getSerializedDocTypes(PsiElement element) {
    return Arrays.stream(element.getNode().getChildren(TokenSet.create(PhpDocElementTypes.phpDocType)))
      .map(ASTNode::getPsi).filter(Objects::nonNull)
      .flatMap(e -> new PhpType().add(e).getTypes().stream())
      .collect(Collectors.joining(TYPES_SEPARATOR));
  }

  @Override
  public @Nullable PhpType complete(String expression, Project project) {
    String callableFQN = ContainerUtil.getFirstItem(PhpParameterBasedTypeProvider.extractSignatures(expression, 2));
    return callableFQN != null ? PhpType.from(callableFQN) : null;
  }

  @Override
  public Collection<? extends PhpNamedElement> getBySignature(String expression, Set<String> visited, int depth, Project project) {
    return null;
  }

  @Override
  public char getKey() {
    return KEY;
  }

  private static boolean isAdvancedCallable(PsiElement element) {
    return ContainerUtil.exists(element.getNode().getChildren(TokenSet.create(PhpDocTokenTypes.DOC_TEXT)), e -> e.getText().equals(":"));
  }

  private static @NotNull String resolveFQN(@NotNull PhpDocType element) {
    String name = element.getName();
    if (name != null && ADVANCED_CALLABLES.contains(PhpLangUtil.toFQN(name))) {
      return PhpLangUtil.toFQN(name);
    }
    PhpNamedElement useElement = ContainerUtil.getOnlyItem(element.resolveLocal());
    return useElement instanceof PhpUse ? useElement.getFQN() : element.getFQN();
  }

  public static boolean isSigned(String s) {
    return s.charAt(1) == KEY;
  }

  @NotNull
  public static PhpType getReturnType(String incompleteAdvancedCallableType) {
    return getType(ContainerUtil.getLastItem(PhpParameterBasedTypeProvider.extractSignatures(incompleteAdvancedCallableType, 2)));
  }

  @NotNull
  public static List<PhpType> getParametersTypes(String incompleteAdvancedCallableType) {
    List<String> signatures = PhpParameterBasedTypeProvider.extractSignatures(incompleteAdvancedCallableType, 2);
    if (signatures.size() != 3) return Collections.emptyList();
    String signature = signatures.get(1);
    return ContainerUtil.map(StringUtil.split(signature, PARAMETERS_SEPARATOR), PsalmAdvancedCallableTypeProvider::getType);
  }

  @NotNull
  private static PhpType getType(String serializedType) {
    PhpType type = new PhpType();
    StringUtil.split(serializedType, TYPES_SEPARATOR).forEach(type::add);
    return type;
  }
}
