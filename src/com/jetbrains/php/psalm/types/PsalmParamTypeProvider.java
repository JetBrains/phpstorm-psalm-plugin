package com.jetbrains.php.psalm.types;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.Function;
import com.intellij.util.ObjectUtils;
import com.jetbrains.php.PhpWorkaroundUtil;
import com.jetbrains.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocMethodTag;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpKeyTypeProvider;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import com.jetbrains.php.lang.psi.resolve.types.generics.PhpGenericTypeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

import static com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes.*;
import static com.jetbrains.php.lang.psi.PhpPsiUtil.isOfType;

public class PsalmParamTypeProvider implements PhpTypeProvider4 {

  @Override
  public char getKey() {
    return 'ᢒ';
  }

  @Override
  public @Nullable PhpType getType(PsiElement element) {
    if (element instanceof PhpDocType) {
      return parsePsalmType(element, ((PhpDocType)element).getName(), DOC_LAB, DOC_RAB, DOC_COMMA, e -> ObjectUtils.tryCast(e, PhpDocType.class));
    }
    else if (element instanceof PhpDocMethodTag) {
      return PhpPsiUtil.getChildren(element, e -> isOfType(e, PhpDocElementTypes.phpDocMethodType)).stream()
        .map(PsalmParamTypeProvider::parsePsalmTypeFromDocMethod)
        .reduce(new PhpType(), PhpType::add);
    }
    return null;
  }

  private static PhpType parsePsalmTypeFromDocMethod(PsiElement docMethodType) {
    PsiElement firstChild = docMethodType.getFirstChild();
    if (firstChild == null) return PhpType.EMPTY;
    return parsePsalmType(docMethodType, firstChild.getText(), PhpTokenTypes.opLESS, PhpTokenTypes.opGREATER,
                          PhpTokenTypes.opCOMMA, PhpGenericTypeProvider::getReferenceFromDocMethodType);
  }
  private static @Nullable PhpType parsePsalmType(@NotNull PsiElement docElement,
                                                  @Nullable String name,
                                                  IElementType lBrace,
                                                  IElementType rBrace,
                                                  IElementType comma,
                                                  @NotNull Function<PsiElement, PhpTypedElement> typedElementExtractor) {
    if (!PhpWorkaroundUtil.isGenericArray(docElement, name)) return null;
    return PhpWorkaroundUtil.valueDocTypes(docElement, lBrace, rBrace, comma)
      .map(typedElementExtractor)
      .map(PhpType::from)
      .reduce(new PhpType(), PhpType::add)
      .map(t -> PhpKeyTypeProvider.isArrayKeySignature(t) ? signWithSameKey(t) : PhpType.pluralise(t, 1));
  }

  @NotNull
  private static String signWithSameKey(@NotNull String t) {
    return t.substring(0, 2) + t;
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
