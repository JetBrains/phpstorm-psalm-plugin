package com.jetbrains.php.psalm.types;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpCharBasedTypeKey;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeSignatureKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.intellij.util.containers.ContainerUtil.immutableList;

public class PsalmExtendedStringDocTypeProvider implements PhpTypeProvider4 {
  private static final Collection<String> EXTENDED_STRINGS = immutableList(
    "class-string"
    ,"callable-string"
    ,"numeric-string"
    ,"trait-string"
  );

  private static final @NotNull PhpType NUMERIC_TYPE = PhpType.builder().add(PhpType.STRING).add(PhpType.INT).add(PhpType.FLOAT).build();

  private static final Map<String, PhpType> ALTERNATIVE_SCALAR_TYPES = Map.of(
    "scalar", PhpType.SCALAR
    ,"numeric", NUMERIC_TYPE
    ,"array-key", PhpType.NUMERIC
    ,"empty", PhpType.MIXED
    ,"closure", PhpType.CLOSURE
    ,"never-return", PhpType.VOID
    ,"never-returns", PhpType.VOID
    ,"no-return", PhpType.VOID
  );

  public static final Collection<String> EXTENDED_SCALAR_TYPES = ContainerUtil.union(EXTENDED_STRINGS, ALTERNATIVE_SCALAR_TYPES.keySet());

  private static final PhpCharBasedTypeKey KEY = new PhpCharBasedTypeKey() {
    @Override
    public char getKey() {
      return 'ᢔ';
    }
  };

  @Override
  public char getKey() {
    return KEY.getKey();
  }

  @Override
  public @Nullable PhpType getType(PsiElement element) {
    if (element instanceof PhpDocType) {
      PhpType type = getScalarType((PhpDocType)element);
      if (!type.isEmpty()) {
        PsiElement attributes = PhpPsiUtil.getChildOfType(element, PhpDocElementTypes.phpDocAttributeList);
        String attributesText = attributes != null ? attributes.getText() : null;
        return type.map(name -> KEY.sign(attributesText != null ? name + "." + attributesText : name));
      }
    }
    return null;
  }

  public @NotNull PhpType getScalarType(PhpDocType element) {
    String name = StringUtil.toLowerCase(element.getName());
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
    String expr = expression.substring(2);
    int lastDot = expr.lastIndexOf('.');
    return new PhpType().add(lastDot > 0 ? expr.substring(0, lastDot) : expr);
  }

  @Override
  public Collection<? extends PhpNamedElement> getBySignature(String expression, Set<String> visited, int depth, Project project) {
    return null;
  }
}
