package com.jetbrains.php.psalm.types;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.lang.PhpLangUtil;
import com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpClassMember;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpCharBasedTypeKey;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class PsalmAliasTypeProvider implements PhpTypeProvider4 {
  private static final PhpCharBasedTypeKey KEY = new PhpCharBasedTypeKey() {
    @Override
    public char getKey() {
      return '\u1792';
    }
  };
  public static final String PSALM_TYPE_TAG_NAME = "@psalm-type";
  public static final String PSALM_IMPORT_TYPE_TAG_NAME = "@psalm-import-type";

  @Override
  public char getKey() {
    return KEY.getKey();
  }

  @Override
  public @Nullable PhpType getType(PsiElement element) {
    if (element instanceof PhpDocType) {
      return resolveTypeAliases((PhpDocType)element).stream()
        .flatMap(e -> PhpPsiUtil.<PhpDocType>getChildren(e, PhpDocType.class::isInstance).stream())
        .map(PhpTypedElement::getType)
        .reduce(new PhpType(), PhpType::add);
    }
    return null;
  }

  public static Collection<PhpDocTag> resolveTypeAliases(PhpDocType phpDocType) {
    String name = phpDocType.getText();
    if (name.indexOf(PhpLangUtil.NAMESPACE_SEPARATOR_CHAR) >= 0) return Collections.emptyList();
    PhpDocComment docComment = PhpPsiUtil.getParentByCondition(phpDocType, PhpDocComment.class::isInstance);
    if (docComment == null) return Collections.emptyList();
    Collection<PhpDocTag> types = resolveLocalTypeFromTypeAlias(name, docComment);
    if (types.isEmpty()) {
      return resolveLocalTypeFromClassTypeAlias(name, docComment.getOwner());
    }
    return types;
  }

  @NotNull
  private static Collection<PhpDocTag> resolveLocalTypeFromClassTypeAlias(String name, PsiElement classMember) {
    if (classMember instanceof PhpClassMember) {
      PhpClass containingClass = ((PhpClassMember)classMember).getContainingClass();
      if (containingClass != null) {
        return resolveLocalTypeFromTypeAlias(name, containingClass.getDocComment());
      }
    }
    return Collections.emptyList();
  }

  @NotNull
  private static Collection<PhpDocTag> resolveLocalTypeFromTypeAlias(String name, @Nullable PhpDocComment docComment) {
    if (docComment == null) return Collections.emptyList();
    return ContainerUtil.filter(docComment.getTagElementsByName(PSALM_TYPE_TAG_NAME),
                                t -> name.equals(getTypeName(t)));
  }

  private static @Nullable String getTypeName(PhpDocTag tag) {
    PsiElement identifier = PhpPsiUtil.getChildOfType(tag, PhpDocTokenTypes.DOC_IDENTIFIER);
    return identifier != null ? identifier.getText() : null;
  }

  @Override
  public @Nullable PhpType complete(String expression, Project project) {
    return null;
  }

  @Override
  public Collection<? extends PhpNamedElement> getBySignature(String expression,
                                                              Set<String> visited,
                                                              int depth,
                                                              Project project) {
    return null;
  }
}
