package com.jetbrains.php.psalm.types;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import com.jetbrains.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpCharBasedTypeKey;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

public class PsalmAdvancedCallableTypeProvider extends PhpCharBasedTypeKey implements PhpTypeProvider4 {

  static final char KEY = '\u1913';

  @Override
  public @Nullable PhpType getType(PsiElement element) {
    if (element instanceof PhpDocType && isAdvancedCallable(element)) {
      PhpType type = new PhpType();
      for (ASTNode child : element.getNode().getChildren(TokenSet.create(PhpDocElementTypes.phpDocType))) {
        PhpDocType returnDocType = child != null ? ObjectUtils.tryCast(child.getPsi(), PhpDocType.class) : null;
        type.add(returnDocType);
      }
      return type.map(this::sign);
    }
    return null;
  }

  @Override
  public @Nullable PhpType complete(String expression, Project project) {
    return PhpType.CLOSURE;
  }

  @Override
  public Collection<? extends PhpNamedElement> getBySignature(String expression, Set<String> visited, int depth, Project project) {
    return null;
  }

  @Override
  public char getKey() {
    return KEY;
  }

  private static boolean isAdvancedCallable(PsiElement element) {
    return ContainerUtil.exists(element.getNode().getChildren(TokenSet.create(PhpDocTokenTypes.DOC_TEXT)), e -> e.getText().equals(":"));
  }
}
