package com.jetbrains.php.psalm.types.generics;

import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.ArrayUtil;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.lang.documentation.phpdoc.PhpCustomDocTagValuesStubProvider;
import com.jetbrains.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.psalm.types.PsalmExtendedTypeProvider;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes.DOC_IDENTIFIER;
import static com.jetbrains.php.psalm.types.PsalmParamTypeProvider.valueDocTypes;

public class PsalmTemplatesCustomDocTagValueStubProvider implements PhpCustomDocTagValuesStubProvider {
  public static final String[] EXTENDED_NAMES = {"@extends", "@template-extends", "@implements", "@template-implements"};

  @Override
  public @Nullable String getCustomValueToSaveIntoStubs(@NotNull PhpDocTag tagElement) {
    String name = tagElement.getName();
    if (ArrayUtil.contains(name, PsalmExtendedTypeProvider.TEMPLATES_NAMES)) {
      PsiElement templateName = PhpPsiUtil.getChildOfType(tagElement.getFirstPsiChild(), DOC_IDENTIFIER);
      if (templateName != null) {
        return templateName.getText();
      }
    }
    else if (ArrayUtil.contains(name, EXTENDED_NAMES)) {
      PhpDocType docType = PhpPsiUtil.getChildByCondition(tagElement, PhpDocType.class::isInstance);
      if (docType != null) {
        String extendedClassFQN = docType.getFQN();
        PsiElement attributeList = PhpPsiUtil.getChildOfType(docType, PhpDocElementTypes.phpDocAttributeList);
        List<String> templateFQN = ContainerUtil.map(PhpPsiUtil.getChildren(attributeList, PhpDocType.class::isInstance), PhpDocType::getFQN);
        if (!templateFQN.isEmpty()) {
          return StreamEx.of(extendedClassFQN)
            .append(templateFQN)
            .collect(Collectors.joining("|"));
        }
      }
    }
    return null;
  }

  @NotNull
  public static Pair<String, List<String>> decodeExtendedClassAndTemplate(String s) {
    List<String> split = StringUtil.split(s, "|");
    return Pair.create(split.get(0), split.subList(1, split.size()));
  }
}
