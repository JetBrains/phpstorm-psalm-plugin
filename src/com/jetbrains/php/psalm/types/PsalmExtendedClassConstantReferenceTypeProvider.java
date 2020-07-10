package com.jetbrains.php.psalm.types;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.PhpLangUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocRef;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.elements.impl.PhpReferenceImpl;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeSignatureKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes.*;

public class PsalmExtendedClassConstantReferenceTypeProvider implements PhpTypeProvider4 {


  private static final Condition<PsiElement> IS_DOC_IDENTIFIER = e -> PhpPsiUtil.isOfType(e, DOC_IDENTIFIER);
  private static final char KEY = 'ᢕ';

  @Override
  public char getKey() {
    return KEY;
  }

  @Override
  public @Nullable PhpType getType(PsiElement element) {
    if (element instanceof PhpDocType) {
      PhpPsiElement child = ((PhpDocType)element).getFirstPsiChild();
      if (child instanceof PhpDocRef) {
        return getType(((PhpDocRef)child));
      }
    } else if (element instanceof PhpDocRef) {
      return getType(((PhpDocRef)element));
    }
    return null;
  }

  private PhpType getType(PhpDocRef docRef) {
    PhpNamespaceReference reference = PhpPsiUtil.getChildByCondition(docRef, PhpNamespaceReference.INSTANCEOF);
    String immediateNamespaceName = reference != null ? reference.getFullName() : "";
    String namespaceName = PhpReferenceImpl.findNamespaceName(immediateNamespaceName, docRef);
    PsiElement docStatic = PhpPsiUtil.getChildOfType(docRef, DOC_STATIC);
    if (docStatic == null) return null;
    PsiElement className = PhpPsiUtil.getPrevSiblingByCondition(docStatic, IS_DOC_IDENTIFIER);
    PsiElement constantName = PhpPsiUtil.getNextSiblingByCondition(docStatic, IS_DOC_IDENTIFIER);
    if (className == null || constantName == null) return null;
    String constantFQN = PhpTypeSignatureKey.CLASS.sign(PhpLangUtil.concat(namespaceName, className.getText())) + "." + constantName.getText();
    return new PhpType().add(isWildcard(constantName) ? sign(constantFQN) : PhpTypeSignatureKey.CLASS_CONSTANT.sign(constantFQN));
  }

  private static boolean isWildcard(PsiElement constantName) {
    PsiElement sibling = constantName.getNextSibling();
    return PhpPsiUtil.isOfType(sibling, DOC_TEXT) && sibling.textMatches("*");
  }

  @NotNull
  private String sign(String name) {
    return "#" + getKey() + name;
  }

  @Override
  public @Nullable PhpType complete(String expression, Project project) {
    return null;
  }

  @Override
  public Collection<? extends PhpNamedElement> getBySignature(String expression, Set<String> visited, int depth, Project project) {
    int separatorIndex = expression.lastIndexOf('.');
    String pattern = expression.substring(separatorIndex + 1);
    return PhpIndex.getInstance(project).getBySignature(expression.substring(0, separatorIndex)).stream()
      .map(e -> e instanceof PhpClass ? ((PhpClass)e) : null).filter(Objects::nonNull)
      .flatMap(c -> c.getFields().stream()).filter(Field::isConstant)
      .filter(e -> StringUtil.startsWith(e.getName(), pattern))
      .collect(Collectors.toSet());
  }

  public static boolean isSigned(String type) {
    return StringUtil.startsWith(type, "#" + KEY);
  }
}
