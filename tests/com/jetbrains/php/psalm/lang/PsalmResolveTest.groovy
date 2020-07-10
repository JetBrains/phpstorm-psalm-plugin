package com.jetbrains.php.psalm.lang

import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiReference
import com.intellij.psi.ResolveResult
import com.intellij.util.containers.ContainerUtil
import com.jetbrains.php.fixtures.PhpCodeInsightFixtureTestCase
import com.jetbrains.php.lang.psi.elements.Constant
import com.jetbrains.php.lang.psi.elements.Field
import com.jetbrains.php.lang.psi.elements.Variable
import com.jetbrains.php.psalm.types.PsalmTypeInferenceTest
import org.junit.Assert

class PsalmResolveTest extends PhpCodeInsightFixtureTestCase{
  @Override
  protected String getTestDataHome() {
    return PsalmTypeInferenceTest.TEST_DATA_HOME
  }

  @Override
  protected String getFixtureTestDataFolder() {
    return "codeInsight/resolve"
  }

  void testExtendedClassConstantReferenceWithWildcard() throws Throwable {
    configure('''
<?php

class C{
    const A_1 = 1;
    const A_2 = "a";
    const B_2 = "a";
}

function f($a, $b)
{
    /** @psalm-var C::<caret>A_* $c */
    $c;
}
''')
    def reference = reference()
    assertInstanceOf(reference, PsiPolyVariantReference.class)
    def multiResolve = reference.multiResolve(false)
    assertSize(2, multiResolve)
    def first = multiResolve[0].getElement()
    def second = multiResolve[1].getElement()
    assertInstanceOf(first, Field.class)
    assertInstanceOf(second, Field.class)
    assertEquals(ContainerUtil.set("\\C.A_1", "\\C.A_2"), ContainerUtil.set(first.getFQN(), second.getFQN()))
  }
}
