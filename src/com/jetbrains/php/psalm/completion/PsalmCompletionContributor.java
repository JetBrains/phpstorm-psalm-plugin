package com.jetbrains.php.psalm.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.psalm.types.PsalmClassStringDocTypeProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class PsalmCompletionContributor extends CompletionContributor {
  private static final Collection<String> PSALM_CUSTOM_DOC_TYPES = Arrays.asList(
    PsalmClassStringDocTypeProvider.CLASS_STRING
    ,"callable-string"
    ,"numeric-string"
    ,"scalar"
    ,"numeric"
  );

  public PsalmCompletionContributor() {
    extend(CompletionType.BASIC, psiElement().withParent(PhpDocType.class), new PsalmCustomDocTypeCompletionProvider());
  }

  private static class PsalmCustomDocTypeCompletionProvider extends CompletionProvider<CompletionParameters> {
    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters,
                                  @NotNull ProcessingContext context,
                                  @NotNull CompletionResultSet result) {
      for (String tag : PSALM_CUSTOM_DOC_TYPES) {
        result.addElement(LookupElementBuilder.create(tag).withBoldness(true));
      }
    }
  }
}
