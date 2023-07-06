package com.jetbrains.php.psalm.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.PhpIcons;
import com.jetbrains.php.PhpWorkaroundUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.documentation.phpdoc.psi.impl.PhpDocTemplateParameter;
import com.jetbrains.php.lang.documentation.phpdoc.psi.impl.tags.PhpDocTemplateTag;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.resolve.types.generics.PhpGenericsExtendedTypeProvider;
import com.jetbrains.php.psalm.types.PsalmExtendedStringDocTypeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.jetbrains.php.lang.psi.resolve.types.generics.PhpGenericsBaseExtendedWithGenericTypeProvider.getTemplateNames;
import static com.jetbrains.php.lang.psi.resolve.types.generics.PhpGenericsTemplatesCustomDocTagValueStubProvider.getTypeNamesPsi;

public class PsalmCompletionContributor extends CompletionContributor implements DumbAware {

  public PsalmCompletionContributor() {
    extend(CompletionType.BASIC, psiElement().withParent(PhpDocType.class), new PsalmCustomDocTypeCompletionProvider());
    extend(CompletionType.BASIC, psiElement().withParent(PhpDocType.class), new PsalmCustomTypesCompletionProvider());
  }

  private static class PsalmCustomDocTypeCompletionProvider extends CompletionProvider<CompletionParameters> {

    private static final Collection<String> TYPES = ContainerUtil.union(ContainerUtil.union(PsalmExtendedStringDocTypeProvider.EXTENDED_SCALAR_TYPES,
                                                                                            PhpWorkaroundUtil.getGenericArraysNames()), Set.of("key-of", "value-of"));

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters,
                                  @NotNull ProcessingContext context,
                                  @NotNull CompletionResultSet result) {
      for (String tag : TYPES) {
        result.addElement(createCustomTypeLookupElement(tag));
      }
    }
  }

  private static class PsalmCustomTypesCompletionProvider extends CompletionProvider<CompletionParameters> {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters,
                                  @NotNull ProcessingContext context,
                                  @NotNull CompletionResultSet result) {
      PhpDocComment docComment = PhpPsiUtil.getParentOfClass(parameters.getPosition(), PhpDocComment.class);
      if (docComment != null) {
        addTemplates(docComment, result);
        Collection<String> customTypes = PhpGenericsExtendedTypeProvider.getCustomTypes(docComment);
        for (String customType : customTypes) {
          result.addElement(createCustomTypeLookupElement(customType));
        }
      }
    }

    private static void addTemplates(PhpDocComment docComment, CompletionResultSet result) {
      PsiElement owner = docComment.getOwner();
      Stream<PsiElement> templates = getTypeNamesPsi(docComment, getTemplateNames());

      PhpClass clazz = PhpPsiUtil.getParentOfClass(owner, PhpClass.class);
      if (clazz != null) {
        templates = Stream.concat(templates, getTypeNamesPsi(clazz.getDocComment(), getTemplateNames()));
      }

      List<LookupElementBuilder> elements = templates
        .map(t -> PhpPsiUtil.getParentOfClass(t, PhpDocTemplateTag.class)).filter(Objects::nonNull)
        .map(tag -> createTemplateLookupElement(tag)).filter(Objects::nonNull)
        .toList();

      result.addAllElements(elements);
    }

    @Nullable
    private static LookupElementBuilder createTemplateLookupElement(PhpDocTemplateTag tag) {
      PhpDocComment tagDocComment = PhpPsiUtil.getParentOfClass(tag, PhpDocComment.class);
      PsiElement tagOwner = tagDocComment != null ? tagDocComment.getOwner() : null;
      String definedAt = "";
      if (tagOwner instanceof PhpClass clazz) {
        definedAt = " of " + clazz.getFQN();
      } else if (tagOwner instanceof Method method) {
        definedAt = " of " + method.getName() + "()";
      }

      PhpDocTemplateParameter templateParameter = tag.getTemplateParameter();
      if (templateParameter == null) return null;
      String name = templateParameter.getName();

      String variantString = tag.isCovariant() ? " covariant" : tag.isContravariant() ? " contravariant" : "";
      String tailText = variantString + " template parameter" + definedAt;

      PhpDocType superType = tag.getSuperType();
      if (superType != null) {
        tailText += " extends " + superType.getText();
      }

      return LookupElementBuilder.create(name)
        .withBoldness(true)
        .withTailText(tailText)
        .withIcon(PhpIcons.TEMPLATE_PARAMETER);
    }
  }

  @NotNull
  private static LookupElementBuilder createCustomTypeLookupElement(String tag) {
    return LookupElementBuilder.create(tag).withBoldness(true);
  }
}
