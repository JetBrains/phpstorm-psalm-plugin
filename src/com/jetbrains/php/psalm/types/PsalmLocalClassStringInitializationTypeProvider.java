package com.jetbrains.php.psalm.types;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.ObjectUtils;
import com.jetbrains.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.elements.impl.ClassReferenceImpl;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

public class PsalmLocalClassStringInitializationTypeProvider implements PhpTypeProvider4 {
  @Override
  public char getKey() {
    // not used, can be changed to anything
    return '0';
  }

  @Override
  public @Nullable PhpType getType(PsiElement element) {
    if (element instanceof NewExpression) {
      ClassReference classReference = ((NewExpression)element).getClassReference();
      Variable variableClassReference = classReference != null ? ObjectUtils.tryCast(classReference.getFirstPsiChild(), Variable.class) : null;
      if (variableClassReference != null) {
        PhpType res = new PhpType();
        StreamEx.of(variableClassReference.resolveLocal()).select(Parameter.class)
          .map(Parameter::getDocTag)
          .flatMap(PsalmLocalClassStringInitializationTypeProvider::docTypes)
          .filter(docType -> "class-string".equals(docType.getName()))
          .flatMap(tag -> docTypes(PhpPsiUtil.getChildOfType(tag, PhpDocElementTypes.phpDocAttributeList))).map(ClassReferenceImpl::getLocalFQN)
          .forEach(classStringArgument -> res.add(classStringArgument));
        return res;
      }
    }
    return null;
  }

  private static Stream<PhpDocType> docTypes(PsiElement tag) {
    return PhpPsiUtil.<PhpDocType>getChildren(tag, PhpDocType.class::isInstance).stream();
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
