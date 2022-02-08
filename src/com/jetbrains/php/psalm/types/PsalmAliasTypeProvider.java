package com.jetbrains.php.psalm.types;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.PhpLangUtil;
import com.jetbrains.php.lang.documentation.phpdoc.PhpDocUtil;
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
import com.jetbrains.php.lang.psi.resolve.types.generics.PhpGenericsExtendedTypeProvider;
import com.jetbrains.php.lang.psi.stubs.indexes.PhpDocTypeAliasesIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes.DOC_IDENTIFIER;

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
      PhpType type = new PhpType();
      for (PhpDocTag tag : resolveTypeAliases((PhpDocType)element)) {
        List<PhpDocType> docType = PhpPsiUtil.getChildren(tag, PhpDocType.class::isInstance);
        if (tag.getName().endsWith("-import-type")) {
          PsiElement aliasName = PhpPsiUtil.getChildOfType(tag, DOC_IDENTIFIER);
          PhpDocType fromType = ContainerUtil.getOnlyItem(docType);
          if (aliasName != null && fromType != null) {
            type.add(KEY.sign(fromType.getFQN() + "." + aliasName.getText()));
          }
        } else {
          docType.forEach(type::add);
        }
      }
      return type;
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
    Collection<PhpDocTag> res = new HashSet<>();
    PhpDocUtil.processTagElementsByNames(docComment, tag -> {
      PsiElement identifier = getAliasIdentifier(tag);
      if (identifier != null && identifier.textMatches(name)) {
        res.add(tag);
      }
      return true;
    }, PSALM_TYPE_TAG_NAME, PSALM_IMPORT_TYPE_TAG_NAME);
    return res;
  }

  @Nullable
  public static PsiElement getAliasIdentifier(PhpDocTag tag) {
    PsiElement identifier = PhpPsiUtil.getChildOfType(tag, PhpDocTokenTypes.DOC_IDENTIFIER);
    if (tag.getName().equals(PSALM_IMPORT_TYPE_TAG_NAME)) {
      return PhpGenericsExtendedTypeProvider.getAliasIdentifierFromImportedType(tag);
    }
    return identifier;
  }

  @Override
  public @Nullable PhpType complete(String expression, Project project) {
    int separator = expression.lastIndexOf('.');
    if (separator < 0) return null;
    String fromClassFQN = expression.substring(2, separator);
    String typeName = expression.substring(separator + 1);
    return PhpDocTypeAliasesIndex.getTypeByAliasName(project, fromClassFQN, typeName);
  }

  @Override
  public Collection<? extends PhpNamedElement> getBySignature(String expression,
                                                              Set<String> visited,
                                                              int depth,
                                                              Project project) {
    return null;
  }
}
