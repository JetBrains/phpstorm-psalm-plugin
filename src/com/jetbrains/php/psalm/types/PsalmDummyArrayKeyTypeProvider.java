package com.jetbrains.php.psalm.types;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.PhpWorkaroundUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.documentation.phpdoc.psi.impl.PhpDocTypeImpl;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpCharBasedTypeKey;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

import static com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes.DOC_COMMA;

public class PsalmDummyArrayKeyTypeProvider implements PhpTypeProvider4 {
  public static final PhpCharBasedTypeKey KEY = new PhpCharBasedTypeKey() {
    @Override
    public char getKey() {
      return '\u1891';
    }
  };

  private final @NotNull PhpType myIntIndices = PhpType.INT.map(KEY::sign);

  @Override
  public char getKey() {
    return KEY.getKey();
  }

  @Override
  public @Nullable PhpType getType(PsiElement element) {
    if (element instanceof PhpDocTypeImpl) {
      String name = PhpWorkaroundUtil.getGenericArrayName(((PhpDocType)element));
      if (name != null && name.equals("list")) {
        return myIntIndices;
      }
      boolean isGenericArray = PhpWorkaroundUtil.isGenericArray(((PhpDocType)element));
      PsiElement separatorElement = PhpWorkaroundUtil.getTypesSeparatorElement(element);
      if (PhpPsiUtil.isOfType(separatorElement, DOC_COMMA)) {
        PhpType keyType = new PhpType().add(PhpPsiUtil.getPrevSiblingIgnoreWhitespace(separatorElement, true));
        if (isGenericArray) {
          if (!"iterable".equalsIgnoreCase(((PhpDocType)element).getName())) {
            keyType = keyType.filterOut(t -> !PhpType.NUMERIC.getTypes().contains(t));
          }
          return keyType.map(KEY::sign);
        } else {
          String fqn = PhpDocTypeImpl.getClassReferenceFQN(((PhpDocType)element), ((PhpDocType)element).getName());
          return keyType.map(s -> s + "." + fqn).map(KEY::sign).add(((PhpDocTypeImpl)element).inferNativeType());
        }
      }
    }
    return null;
  }

  @Override
  public @Nullable PhpType complete(String expression, Project project) {
    return null;
  }

  @Override
  public Collection<? extends PhpNamedElement> getBySignature(String expression, Set<String> visited, int depth, Project project) {
    return null;
  }
}
