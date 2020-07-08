package com.jetbrains.php.psalm.types;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.ObjectUtils;
import com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import com.jetbrains.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.documentation.phpdoc.psi.impl.PhpDocTypeImpl;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import static com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes.*;
import static com.jetbrains.php.lang.psi.PhpPsiUtil.isOfType;

public class PsalmParamTypeProvider implements PhpTypeProvider4 {

  @Override
  public char getKey() {
    return 'ᢒ';
  }

  @Override
  public @Nullable PhpType getType(PsiElement element) {
    return element instanceof PhpDocType ? parsePsalmType((PhpDocType)element) : null;
  }

  private static PhpType parsePsalmType(@NotNull PhpDocType type) {
    PsalmType psalmType = parsePsalmType(type, 0);
    return psalmType.parsed() ? psalmType.getType() : null;
  }

  private static class PsalmType extends Pair<PhpType, Boolean> {
    private static final PsalmType NOT_PARSED = new PsalmType(PhpType.EMPTY, false);

    private PsalmType(PhpType type, boolean parsedArray) {
      super(type, parsedArray);
    }

    public PhpType getType() {
      return first;
    }

    private boolean parsed() {
      return second;
    }
  }

  public static PsalmType parsePsalmType(@NotNull PhpDocType docType, int depth) {
    String name = StringUtil.notNullize(docType.getName());
    if (!"array".equalsIgnoreCase(name) && !"list".equalsIgnoreCase(name)) {
      return new PsalmType(PhpDocTypeImpl.getLocalType(docType).pluralise(depth), false);
    }
    PhpType typeFromDocTypeValues = valueDocTypeTags(docType)
      .map(t -> parsePsalmType(t, depth + 1))
      .map(PsalmType::getType)
      .reduce(new PhpType(), PhpType::add);
    return !typeFromDocTypeValues.isEmpty() ? new PsalmType(typeFromDocTypeValues, true) : PsalmType.NOT_PARSED;
  }

  @NotNull
  public static Stream<PhpDocType> valueDocTypeTags(@NotNull PhpDocType docType) {
    PsiElement attributes = PhpPsiUtil.getChildOfType(docType, PhpDocElementTypes.phpDocAttributeList);
    if (attributes == null) {
      return Stream.empty();
    }
    PsiElement attributesFirstChild = attributes.getFirstChild();
    if (!(isOfType(attributesFirstChild, DOC_LAB) && isOfType(attributes.getLastChild(), DOC_RAB))) {
      return Stream.empty();
    }
    PsiElement elementToFindCommentsAfter = ObjectUtils.notNull(PhpPsiUtil.getChildOfType(attributes, DOC_COMMA), attributesFirstChild);
    return PhpPsiUtil.<PhpDocType>getChildren(attributes, PhpDocType.class::isInstance).stream()
      .filter(t -> t.getTextOffset() > elementToFindCommentsAfter.getTextOffset());
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
