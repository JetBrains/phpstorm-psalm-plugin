package com.jetbrains.php.psalm.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.psalm.types.PsalmExtendedStringDocTypeProvider;
import com.jetbrains.php.psalm.types.PsalmExtendedTypeProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class PsalmCompletionContributor extends CompletionContributor {

  public PsalmCompletionContributor() {
    extend(CompletionType.BASIC, psiElement().withParent(PhpDocType.class), new PsalmCustomDocTypeCompletionProvider());
    extend(CompletionType.BASIC, psiElement().withParent(PhpDocType.class), new PsalmCustomTypesCompletionProvider());
  }

  private static class PsalmCustomDocTypeCompletionProvider extends CompletionProvider<CompletionParameters> {
    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters,
                                  @NotNull ProcessingContext context,
                                  @NotNull CompletionResultSet result) {
      for (String tag : PsalmExtendedStringDocTypeProvider.EXTENDED_SCALAR_TYPES) {
        result.addElement(createCustomTypeLookupElement(tag));
      }
    }
  }

  private static class PsalmCustomTypesCompletionProvider extends CompletionProvider<CompletionParameters> {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters,
                                  @NotNull ProcessingContext context,
                                  @NotNull CompletionResultSet result) {
      PhpDocComment docComment = PhpPsiUtil.getParentByCondition(parameters.getPosition(), PhpDocComment.INSTANCEOF);
      if (docComment != null) {
        Set<String> customTypes =
          ContainerUtil.union(PsalmExtendedTypeProvider.getTemplates(docComment), PsalmExtendedTypeProvider.getCustomTypes(docComment));
        for (String customType : customTypes) {
          result.addElement(createCustomTypeLookupElement(customType));
        }
      }
    }
  }

  @NotNull
  private static LookupElementBuilder createCustomTypeLookupElement(String tag) {
    return LookupElementBuilder.create(tag).withBoldness(true);
  }
}
