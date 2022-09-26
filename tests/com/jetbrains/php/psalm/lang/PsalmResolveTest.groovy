package com.jetbrains.php.psalm.lang

import com.intellij.psi.PsiPolyVariantReference
import com.intellij.util.containers.ContainerUtil
import com.jetbrains.php.fixtures.PhpCodeInsightFixtureTestCase
import com.jetbrains.php.lang.psi.elements.Field
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.psalm.types.PsalmTypeInferenceTest

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
    assertEquals(Set.of("\\C.A_1", "\\C.A_2"), Set.of(first.getFQN(), second.getFQN()))
  }

  void testExtendedClassConstantReferenceWithFullWildcard() throws Throwable {
    configure('''
<?php

class C{
    const A_1 = 1;
    const A_2 = "a";
    const B_2 = "a";
}

function f($a, $b)
{
    /** @psalm-var C::<caret>* $c */
    $c;
}
''')
    def reference = reference()
    assertInstanceOf(reference, PsiPolyVariantReference.class)
    def multiResolve = reference.multiResolve(false)
    assertSize(3, multiResolve)
    def fqns = ContainerUtil.map2Set(multiResolve, { assertInstanceOf(it.getElement(), Field.class).getFQN() })
    assertEquals(Set.of("\\C.A_1", "\\C.A_2", "\\C.B_2"), fqns)
  }


  void testResolveAliasReferenceToType() throws Throwable {
    configure('''
<?php
/**
 * @psalm-type A_OR_B = A|B
 * @psalm-type CoolType = <caret>A_OR_B|null
 * @return CoolType
 */
function foo($a) {
  
}
''')
    def resolved = resolve()
    assertEquals("A_OR_B", resolved.getText())
    assertEquals("@psalm-type A_OR_B = A|B", resolved.getParent().getText())
  }

  void testResolveImportedAliasReferenceToType() throws Throwable {
    configure('''
<?php
/**
 * @psalm-import-type FooAlias from Phone as MyFooAlias
 * @psalm-type CoolType = <caret>MyFooAlias|null
 */
function foo($a) {
  
}
''')
    def resolved = resolve()
    assertEquals("MyFooAlias", resolved.getText())
    assertEquals("@psalm-import-type FooAlias from Phone as MyFooAlias", resolved.getParent().getText())
  }

  void testResolveImportedAliasFromMultilineReferenceToType() throws Throwable {
    configure('''
<?php
/**
 * @psalm-import-type FooAlias from Phone as MyFooAlias
 * @psalm-type FileMapType = array{
 *      0: <caret>MyFooAlias
 * }
 */
function foo($a) {
  
}
''')
    def resolved = resolve()
    assertEquals("MyFooAlias", resolved.getText())
    assertEquals("@psalm-import-type FooAlias from Phone as MyFooAlias", resolved.getParent().getText())
  }

  void testResolveImportedAliasFromPluralReferenceToType() throws Throwable {
    configure('''
<?php
/**
 * @psalm-import-type FooAlias from Phone as MyFooAlias
 * @psalm-type FileMapType = <caret>MyFooAlias[]
 */
function foo($a) {
  
}
''')
    def resolved = resolve()
    assertEquals("MyFooAlias", resolved.getText())
    assertEquals("@psalm-import-type FooAlias from Phone as MyFooAlias", resolved.getParent().getText())
  }



  void testResolveImportedFromStatement() throws Throwable {
    configure('''
<?php
/**
 * @psalm-import-type FooAlias from Phone as MyFooAlias
 */
function foo($a) {
    /** @var <caret>MyFooAlias $alias */
    $phone = $param->toArray();
}
''')
    def resolved = resolve()
    assertEquals("MyFooAlias", resolved.getText())
    assertEquals("@psalm-import-type FooAlias from Phone as MyFooAlias", resolved.getParent().getText())
  }

  void testResolveImportedFromStatementToContainingClass() throws Throwable {
    configure('''
<?php
/**
 * @psalm-import-type FooAlias from Phone as MyFooAlias
 */
class a {
function foo($a) {
    /** @var <caret>MyFooAlias $alias */
    $phone = $param->toArray();
}
}
''')
    def resolved = resolve()
    assertEquals("MyFooAlias", resolved.getText())
    assertEquals("@psalm-import-type FooAlias from Phone as MyFooAlias", resolved.getParent().getText())
  }

  void testResolveAliasToReferenceTypeFromClosure() throws Throwable {
    configure('''
<?php
/**
 * @psalm-import-type FooAlias from Phone as MyFooAlias
 */
class a {
function foo($a) {
        /**
         * @psalm-return <caret>MyFooAlias
         */
        $param = function () {};
}
}
''')
    def resolved = resolve()
    assertEquals("MyFooAlias", resolved.getText())
    assertEquals("@psalm-import-type FooAlias from Phone as MyFooAlias", resolved.getParent().getText())
  }


  void testResolveImportedFromMemberToContainingClass() throws Throwable {
    configure('''
<?php
/**
 * @psalm-import-type FooAlias from Phone as MyFooAlias
 */
class a {
/**
 * @param $param
 * @return <caret>MyFooAlias
 */
function foo($a) {}
}
''')
    def resolved = resolve()
    assertEquals("MyFooAlias", resolved.getText())
    assertEquals("@psalm-import-type FooAlias from Phone as MyFooAlias", resolved.getParent().getText())
  }

  void testResolveImportedFromClass() throws Throwable {
    configure('''
<?php
/**
 * @psalm-import-type FooAlias from Phone as MyFooAlias
 * @psalm-type TestType = array{base: <caret>MyFooAlias, description: string}  
 */
class a {
function foo($a) {
}
''')
    def resolved = resolve()
    assertEquals("MyFooAlias", resolved.getText())
    assertEquals("@psalm-import-type FooAlias from Phone as MyFooAlias", resolved.getParent().getText())
  }

  void "test find constant by special self class name in @see tag"() {
    configure('''
<?php

namespace {
    /**
     * @see <caret>self::foo()
     */
    class C {
       function foo();
    }
}''')

    def phpClass = assertInstanceOf(resolve(), PhpClass.class)
    assertEquals "\\C", phpClass.getFQN()
  }

  void "test find constant by special parent class name in @see tag"() {
    configure('''
<?php

namespace {

    class B {}
    /**
     * @see <caret>parent::foo()
     */
    class C extends B {
       function foo();
    }
}''')

    def phpClass = assertInstanceOf(resolve(), PhpClass.class)
    assertEquals "\\B", phpClass.getFQN()
  }

}
