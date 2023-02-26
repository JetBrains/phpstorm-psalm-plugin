package com.jetbrains.php.psalm.types;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.lang.PhpLangUtil;
import com.jetbrains.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpCharBasedTypeKey;
import com.jetbrains.php.lang.psi.resolve.types.PhpCharTypeKey;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class PsalmExtendedStringDocTypeProvider implements PhpTypeProvider4 {
  private static final Collection<String> EXTENDED_STRINGS = List.of(
    "class-string"
    ,"callable-string"
    ,"numeric-string"
    ,"non-empty-string"
    ,"trait-string"
    ,"literal-string"
  );

  private static final @NotNull PhpType NUMERIC_TYPE = PhpType.builder().add(PhpType.STRING).add(PhpType.INT).add(PhpType.FLOAT).build();

  public static final Map<String, PhpType> NO_RETURN_TYPES = Map.of(
    "never-return", PhpType.VOID
    ,"never-returns", PhpType.VOID
    ,"no-return", PhpType.VOID
  );

  public static final Map<String, PhpType> INT_ALIASES = Map.of(
    "positive-int", PhpType.INT
    ,"non-positive-int", PhpType.INT
    ,"negative-int", PhpType.INT
    ,"non-negative-int", PhpType.INT
  );

  private static final Map<String, PhpType> ALTERNATIVE_SCALAR_TYPES = ContainerUtil.union(ContainerUtil.union(
    Map.of(
      "scalar", PhpType.SCALAR
      ,"numeric", NUMERIC_TYPE
      ,"array-key", PhpType.NUMERIC
      ,"empty", PhpType.MIXED
      ,"closed-resource", PhpType.RESOURCE
      ,"int-mask-of", PhpType.INT
      ,"int-mask", PhpType.INT
    ),
    INT_ALIASES),
    NO_RETURN_TYPES
  );

  public static final Collection<String> EXTENDED_SCALAR_TYPES = ContainerUtil.union(EXTENDED_STRINGS, ALTERNATIVE_SCALAR_TYPES.keySet());

  private static final PhpCharBasedTypeKey KEY = new PhpCharTypeKey('ᢔ');

  @Override
  public char getKey() {
    return KEY.getKey();
  }

  @Override
  public @Nullable PhpType getType(PsiElement element) {
    if (element instanceof PhpDocType) {
      String name = StringUtil.toLowerCase(((PhpDocType)element).getName());
      PhpType type = getScalarType(name);
      if (name != null && !type.isEmpty()) {
        PsiElement attributes = PhpPsiUtil.getChildOfType(element, PhpDocElementTypes.phpDocAttributeList);
        PhpPsiElement attributeContent = attributes instanceof PhpPsiElement ? ((PhpPsiElement)attributes).getFirstPsiChild() : null;
        String attributesText = getAttributesText(attributeContent);
        return PhpType.from(KEY.sign(attributesText != null ? PhpType.createParametrizedType(name, attributesText) : name));
      }
    }
    return null;
  }

  @Nullable
  private static String getAttributesText(PhpPsiElement attributeContent) {
    if (attributeContent instanceof PhpDocType) {
      String type = ContainerUtil.getOnlyItem(((PhpDocType)attributeContent).getType().getTypesWithParametrisedParts());
      if (type != null) {
        return type;
      }
    }
    return attributeContent != null ? attributeContent.getText() : null;
  }

  @NotNull private static PhpType getScalarType(String name) {
    if (ALTERNATIVE_SCALAR_TYPES.containsKey(name)) {
      return ALTERNATIVE_SCALAR_TYPES.get(name);
    }
    else if (EXTENDED_STRINGS.contains(name)) {
      return PhpType.STRING;
    }
    return PhpType.EMPTY;
  }

  @Override
  public @Nullable PhpType complete(String expression, Project project) {
    String expr = PhpLangUtil.toPresentableFQN(expression.substring(2));
    int dimension = PhpType.getPluralDimension(expression);
    expr = PhpType.unpluralize(expr, dimension);
    return getScalarType(PhpType.removeParametrisedType(expr)).pluralise(dimension);
  }

  @Override
  public Collection<? extends PhpNamedElement> getBySignature(String expression, Set<String> visited, int depth, Project project) {
    return null;
  }
}
