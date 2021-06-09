package com.jetbrains.php.psalm.types.generics;

import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.resolve.types.PhpMetaSignatureResolver;
import com.jetbrains.php.lang.psi.resolve.types.PhpParameterBasedTypeProvider;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeSignatureKey;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.intellij.openapi.util.text.StringUtil.*;
import static com.jetbrains.php.lang.psi.resolve.types.PhpParameterBasedTypeProvider.findMappedParameter;

public class PsalmGenericMetaSignatureResolver implements PhpMetaSignatureResolver {
  public static final String PSALM_GENERIC_ARGUMENT = "~";

  @Override
  public @NotNull String getKey() {
    return PSALM_GENERIC_ARGUMENT;
  }

  @Override
  public String getTypeBySignature(PhpIndex index,
                                   @NotNull String signature,
                                   Collection<PhpParameterBasedTypeProvider.PhpPassedArgument> passedArguments) {
    List<String> psalmArguments = split(signature, ".");
    String mappedParameter = findMappedParameter(passedArguments, parseInt(psalmArguments.get(0), Integer.MAX_VALUE));
    if (isEmpty(mappedParameter)) return null;
    Map<String, List<String>>
      extendedClassesToSubstitutedTemplates = PsalmBaseExtendedWithGenericTypeProvider.getExtendedClassesToSubstitutedTemplates(index, PhpTypeSignatureKey.CLASS.signIfUnsigned(mappedParameter));
    List<String> substitutedTemplates = extendedClassesToSubstitutedTemplates.get(psalmArguments.get(2));
    if (substitutedTemplates == null || substitutedTemplates.isEmpty()) return null;
    int substitutedTemplateIndex = parseInt(psalmArguments.get(1), Integer.MAX_VALUE);
    if (substitutedTemplateIndex < substitutedTemplates.size()) {
      return substitutedTemplates.get(substitutedTemplateIndex);
    }
    return null;
  }
}
