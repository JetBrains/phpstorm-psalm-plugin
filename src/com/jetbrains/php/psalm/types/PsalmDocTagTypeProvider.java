package com.jetbrains.php.psalm.types;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

public class PsalmDocTagTypeProvider implements PhpTypeProvider4 {
  private static final String PARAM = "@psalm-param";
  private static final String RETURN = "@psalm-return";
  private static final String VAR = "@psalm-var";

  @Override
  public char getKey() {
    return 'ᢓ';
  }

  @Override
  public @Nullable PhpType getType(PsiElement element) {
    if (element instanceof Parameter) {
      return getTypeFromCustomTag((Parameter)element, PARAM);
    }

    if (element instanceof Function) {
      return getTypeFromCustomTag((Function)element, RETURN);
    }

    if (element instanceof Field || element instanceof Variable) {
      return getTypeFromCustomTag((PhpNamedElement)element, VAR);
    }
    return null;
  }

  @Nullable
  public PhpType getTypeFromCustomTag(PhpNamedElement element, String tagName) {
    PhpDocComment docComment = element.getDocComment();
    PhpDocTag[] tags = docComment != null ? docComment.getTagElementsByName(tagName) : PhpDocTag.EMPTY_ARRAY;
    if (tags.length > 0) {
      PhpType result = new PhpType();
      for (PhpDocTag tag : tags) {
        result.add(tag);
      }
      return result;
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
