package com.jetbrains.php.psalm.types.generics;

import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.ArrayUtil;
import com.jetbrains.php.lang.documentation.phpdoc.PhpCustomDocTagValuesStubProvider;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.psalm.types.PsalmExtendedTypeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes.DOC_IDENTIFIER;
import static com.jetbrains.php.psalm.types.PsalmParamTypeProvider.valueDocTypes;

public class PsalmTemplatesCustomDocTagValueStubProvider implements PhpCustomDocTagValuesStubProvider {
  public static final String[] EXTENDED_NAMES = {"@extends", "@template-extends"};

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
        String templateFQN = valueDocTypes(docType).map(PhpDocType::getFQN).first();
        if (templateFQN != null) {
          return encodeExtendedClassAndTemplate(extendedClassFQN, templateFQN);
        }
      }
    }
    return null;
  }

  @NotNull
  private static String encodeExtendedClassAndTemplate(String extendedClassFQN, String templateFQN) {
    return extendedClassFQN + "|" + templateFQN;
  }

  @NotNull
  public static Pair<String, String> decodeExtendedClassAndTemplate(String s) {
    List<String> split = StringUtil.split(s, "|");
    return Pair.create(split.get(0), split.get(1));
  }
}
