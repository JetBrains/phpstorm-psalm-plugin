package com.jetbrains.php.psalm.types;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

import static com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes.DOC_IDENTIFIER;

public class PsalmExtendedTypeProvider implements PhpTypeProvider4 {
  @Override
  public char getKey() {
    return '\u1890';
  }

  @Override
  public @Nullable PhpType getType(PsiElement element) {
    if (element instanceof PhpDocType) {
      PhpDocComment docComment = PhpPsiUtil.getParentByCondition(element, PhpDocComment.INSTANCEOF);
      if (getTemplateNames(docComment).contains(((PhpDocType)element).getName())) {
        return PhpType.MIXED;
      }
    }
    return null;
  }

  private static Collection<String> getTemplateNames(@Nullable PhpDocComment docComment) {
    if (docComment == null) {
      return Collections.emptySet();
    }
    return Arrays.stream(docComment.getTagElementsByName("@template"))
      .map(PhpPsiElement::getFirstPsiChild).filter(Objects::nonNull)
      .map(value -> PhpPsiUtil.getChildOfType(value, DOC_IDENTIFIER)).filter(Objects::nonNull)
      .map(PsiElement::getText)
      .collect(Collectors.toSet());
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
