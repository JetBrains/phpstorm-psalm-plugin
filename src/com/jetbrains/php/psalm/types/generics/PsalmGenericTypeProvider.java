package com.jetbrains.php.psalm.types.generics;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.elements.impl.ParameterImpl;
import com.jetbrains.php.lang.psi.resolve.types.*;
import com.jetbrains.php.lang.psi.stubs.indexes.expectedArguments.PhpArrayShapeEntriesIndex;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class PsalmGenericTypeProvider implements PhpTypeProvider4 {
  public static final PhpCharBasedTypeKey KEY = new PhpCharBasedTypeKey() {
    @Override
    public char getKey() {
      return 'q';
    }
  };
  @Override
  public char getKey() {
    return KEY.getKey();
  }

  @Override
  public @Nullable PhpType getType(PsiElement element) {
    Collection<PhpDocTag> tags = element instanceof PhpTypedElement ? ((PhpTypedElement)element).getDocTags() : Collections.emptyList();
    for (PhpDocTag tag : tags) {
      String parts = PsalmTemplatesCustomDocTagValueStubProvider.templatesParts(tag).collect(Collectors.joining("|"));
      if (StringUtil.isNotEmpty(parts)) {
        String signature = getSignature(element);
        if (signature != null) {
          return new PhpType().add(KEY.sign(PhpParameterBasedTypeProvider.wrapTypes(Arrays.asList(signature, parts))));
        }
      }
    }
    return null;
  }

  public static @Nullable String getSignature(PsiElement element) {
    if (element instanceof Function) {
      return PhpTypeSignatureKey.getSignature(((Function)element));
    }
    if (element instanceof Variable) {
      return ((Variable)element).getSignature();
    }
    if (element instanceof Parameter) {
      Function function = PhpPsiUtil.getParentByCondition(element, true, Function.INSTANCEOF);
      return ParameterImpl.getParameterSignature(((Parameter)element), function);
    }
    if (element instanceof Field) {
      return PhpTypeSignatureKey.FIELD.sign(((Field)element).getFQN());
    }
    return null;
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
    String signature = ContainerUtil.getFirstItem(PhpParameterBasedTypeProvider.extractSignatures(expression, 0));
    return signature != null ? PhpIndex.getInstance(project).getBySignature(signature, visited, depth) : Collections.emptyList();
  }
}
