package com.jetbrains.php.psalm.types;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.PhpLangUtil;
import com.jetbrains.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocVariable;
import com.jetbrains.php.lang.documentation.phpdoc.psi.impl.PhpDocTypeImpl;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpUse;
import com.jetbrains.php.lang.psi.elements.impl.PhpExpressionImpl;
import com.jetbrains.php.lang.psi.resolve.types.PhpCharBasedTypeKey;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class PsalmAdvancedCallableTypeProvider extends PhpCharBasedTypeKey implements PhpTypeProvider4 {

  public static final Collection<String> ADVANCED_CALLABLES = Set.of(PhpType._CLOSURE, PhpType._CALLABLE);

  @Override
  public @Nullable PhpType getType(PsiElement element) {
    if (element instanceof PhpDocType docType && PhpDocTypeImpl.isAdvancedCallable(docType)) {
      String fqn = resolveFQN(docType);
      if (!ADVANCED_CALLABLES.contains(fqn)) {
        return null;
      }
      String returnType = getSerializedDocTypes(element);
      List<String> parameterTypes = ContainerUtil.map(PhpPsiUtil.<PhpDocParamTag>getChildren(element, PhpDocParamTag.class::isInstance),
                                                      tag -> PhpExpressionImpl.serializeAdvancedClosureParameter(getName(tag), getSerializedDocTypes(tag)));
      return new PhpType().add(PhpType.createParametrizedType(fqn, ContainerUtil.append(parameterTypes, returnType)));
    }
    return null;
  }

  @NotNull
  private static String getName(PhpDocParamTag tag) {
    PhpDocVariable nameIdentifier = PhpPsiUtil.getChildByCondition(tag, PhpDocVariable.class::isInstance);
    return nameIdentifier != null ? nameIdentifier.getName() : "";
  }

  private static String getSerializedDocTypes(PsiElement element) {
    return Arrays.stream(element.getNode().getChildren(TokenSet.create(PhpDocElementTypes.phpDocType)))
      .map(ASTNode::getPsi).filter(Objects::nonNull)
      .flatMap(e -> new PhpType().add(e).getTypes().stream())
      .collect(Collectors.joining(PhpExpressionImpl.CALLABLE_PARAMS_TYPES_SEPARATOR));
  }

  @Override
  public @Nullable PhpType complete(String expression, Project project) {
    return PhpExpressionImpl.getTypeFromSerializedClosureParameter(expression).getSecond().removeParametrisedParts();
  }

  @Override
  public Collection<? extends PhpNamedElement> getBySignature(String expression, Set<String> visited, int depth, Project project) {
    PhpIndex index = PhpIndex.getInstance(project);
    return PhpExpressionImpl.getTypeFromSerializedClosureParameter(expression).getSecond().getTypesWithParametrisedParts().stream()
      .flatMap(c -> PhpType.isSignedType(c) ? index.getBySignature(c, visited, depth).stream() : index.getAnyByFQN(c).stream())
      .collect(Collectors.toSet());
  }

  @Override
  public char getKey() {
    return PhpExpressionImpl.CALLABLE_PARAMS_TYPES_SEPARATOR.charAt(0);
  }

  private static @NotNull String resolveFQN(@NotNull PhpDocType element) {
    String name = element.getName();
    if (name != null && ADVANCED_CALLABLES.contains(PhpLangUtil.toFQN(name))) {
      return PhpLangUtil.toFQN(name);
    }
    PhpNamedElement useElement = ContainerUtil.getOnlyItem(element.resolveLocal());
    return useElement instanceof PhpUse ? useElement.getFQN() : element.getFQN();
  }
}
