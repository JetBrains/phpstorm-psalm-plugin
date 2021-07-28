package com.jetbrains.php.psalm.types.generics;

import com.intellij.openapi.project.Project;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpClassMember;
import com.jetbrains.php.lang.psi.resolve.types.PhpCharBasedTypeKey;
import com.jetbrains.php.lang.psi.resolve.types.PhpIteratedAccessTP;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class PsalmArrayAccessExtendedWithGenericTypeProvider extends PsalmBaseExtendedWithGenericTypeProvider {
  @Override
  protected @NotNull PhpCharBasedTypeKey getSignatureKey() {
    return PhpIteratedAccessTP.TYPE_KEY;
  }

  @Override
  public @Nullable PhpType complete(String expression, Project project) {
    PhpType type = super.complete(expression, project);
    if (type != null) {
      return type.unpluralize();
    }
    return null;
  }

  private static boolean extendsIteratorAggregate(PhpClass containingClass) {
    return PhpType.findSuper("\\IteratorAggregate", containingClass.getFQN(), PhpIndex.getInstance(containingClass.getProject()));
  }

  @Override
  protected StreamEx<? extends PhpClassMember> resolveTargetMembers(String expression,
                                                                    PhpIndex index,
                                                                    Map<String, List<String>> extendedClassesToSubstitutedTemplates) {
    return StreamEx.of(extendedClassesToSubstitutedTemplates.keySet())
      .flatMap(fqn -> index.getAnyByFQN(fqn).stream())
      .filter(PsalmArrayAccessExtendedWithGenericTypeProvider::extendsIteratorAggregate)
      .map(c -> c.findMethodByName("getIterator"))
      .nonNull();
  }

  @Override
  protected @Nullable String getClassRef(String expression) {
    return expression.substring(2);
  }

  @Override
  public boolean interceptsNativeSignature() {
    return true;
  }
}
