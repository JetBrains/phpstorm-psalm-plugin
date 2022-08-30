package com.jetbrains.php.psalm.types;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpArrayKeyAccessTP;
import com.jetbrains.php.lang.psi.resolve.types.PhpCharBasedTypeKey;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class PsalmKeyValueOfDocTypeProvider implements PhpTypeProvider4 {
  private static final PhpCharBasedTypeKey KEY = new PhpCharBasedTypeKey() {
    @Override
    public char getKey() {
      return '\u1914';
    }
  };

  @Override
  public char getKey() {
    return KEY.getKey();
  }

  @Override
  public @Nullable PhpType getType(PsiElement element) {
    if (element instanceof PhpDocType) {
      String name = StringUtil.toLowerCase(((PhpDocType)element).getName());
      if ("key-of".equals(name)) {
        return PhpArrayKeyAccessTP.getArrayKeyType(PhpType.from(getArrayElement(((PhpDocType)element))));
      }
      else if ("value-of".equals(name)) {
        return PhpType.from(getArrayElement(((PhpDocType)element))).map(KEY::sign);
      }
    }
    return null;
  }

  private static @Nullable PhpDocType getArrayElement(PhpDocType element) {
    return PsiTreeUtil.findChildOfType(element, PhpDocType.class);
  }

  @Override
  public @Nullable PhpType complete(String expression, Project project) {
    String signature = expression.substring(2);
    PhpIndex index = PhpIndex.getInstance(project);
    Collection<? extends PhpNamedElement> elements = PhpType.global(project, signature).getTypesWithParametrisedParts().stream()
      .flatMap(fqn -> index.getAnyByFQN(fqn).stream())
      .collect(Collectors.toSet());
    if (!elements.isEmpty() && ContainerUtil.all(elements, e -> e instanceof PhpClass && ((PhpClass)e).isEnum())) {
      return elements.stream()
        .map(e -> ((PhpClass)e).getBackedEnumType())
        .reduce(PhpType::or).orElse(PhpType.EMPTY);
    }
    return PhpType.from(expression.substring(2)).elementType();
  }

  @Override
  public Collection<? extends PhpNamedElement> getBySignature(String expression, Set<String> visited, int depth, Project project) {
    return null;
  }
}
