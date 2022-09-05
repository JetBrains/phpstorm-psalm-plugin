package com.jetbrains.php.psalm.types;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.elements.impl.ClassReferenceImpl;
import com.jetbrains.php.lang.psi.resolve.PhpExpressionClassNamesProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PsalmDocTypeClassNamesExpressionProvider implements PhpExpressionClassNamesProvider {
  @Override
  public Collection<@Nullable String> getClassNames(@NotNull PsiElement element) {
    if (element instanceof Parameter) {
      return getClassNames(((Parameter)element).getDocTag());
    }
    else if (element instanceof Variable) {
      return ((Variable)element).getDocTags().stream()
        .flatMap(tag -> getClassNames(tag).stream())
        .collect(Collectors.toSet());
    }
    return Collections.emptySet();
  }

  private static Collection<@Nullable String> getClassNames(PhpDocTag tag) {
    Collection<@Nullable String> res = new HashSet<>();
    docTypes(tag)
      .filter(docType -> "class-string".equals(docType.getName()))
      .flatMap(t -> docTypes(PhpPsiUtil.getChildOfType(t, PhpDocElementTypes.phpDocAttributeList)))
      .flatMap(t -> t.getType().filterUnknown().types())
      .forEach(classStringArgument -> res.add(classStringArgument));
    if (res.isEmpty()) {
      return Collections.singletonList(null);
    }
    return res;
  }

  private static Stream<PhpDocType> docTypes(PsiElement tag) {
    return PhpPsiUtil.<PhpDocType>getChildren(tag, PhpDocType.class::isInstance).stream();
  }
}
