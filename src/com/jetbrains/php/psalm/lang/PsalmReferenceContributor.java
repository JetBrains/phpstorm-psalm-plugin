package com.jetbrains.php.psalm.lang;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.PhpDocRefReferenceContributor;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocRef;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.psalm.types.PsalmExtendedClassConstantReferenceTypeProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class PsalmReferenceContributor extends PsiReferenceContributor {
  @Override
  public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
    registrar.registerReferenceProvider(psiElement(PhpDocRef.class).withParent(PhpDocType.class),
                                        new PsalmExtendedClassConstantReferenceReferenceProvider());
  }

  private static class PsalmExtendedClassConstantReferenceReferenceProvider extends PsiReferenceProvider {
    @Override
    public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
      String signature = getSignature(element);
      if (signature != null && PsalmExtendedClassConstantReferenceTypeProvider.isSigned(signature)) {
        return new PsiReference[]{new PsalmExtendedClassConstantReference(element)};
      }
      return PsiReference.EMPTY_ARRAY;
    }
  }

  private static String getSignature(PsiElement element) {
    PsiElement parent = element.getParent();
    if (element instanceof PhpDocRef && parent instanceof PhpDocType) {
      return ContainerUtil.getFirstItem(((PhpDocType)parent).getType().getTypes());
    }
    return null;
  }
  private static class PsalmExtendedClassConstantReference extends PsiPolyVariantReferenceBase<PsiElement> {

    private PsalmExtendedClassConstantReference(@NotNull PsiElement psiElement) {
      super(psiElement);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
      String signature = getSignature(myElement);
      if (signature == null) return ResolveResult.EMPTY_ARRAY;
      Collection<? extends PhpNamedElement> constants = PhpIndex.getInstance(myElement.getProject()).getBySignature(signature);
      return ContainerUtil.map2Array(constants, PsiElementResolveResult.class, PsiElementResolveResult::new);
    }

    @Override
    public @NotNull TextRange getRangeInElement() {
      return new TextRange(0, myElement.getTextLength());
    }
  }
}
