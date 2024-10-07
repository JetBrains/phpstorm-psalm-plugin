package com.jetbrains.php.psalm.lang;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.fixtures.PhpCodeInsightFixtureTestCase;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.psalm.types.PsalmTypeInferenceTest;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class PsalmResolveTest extends PhpCodeInsightFixtureTestCase {
  @Override
  protected @NotNull String getTestDataHome() {
    return PsalmTypeInferenceTest.TEST_DATA_HOME;
  }

  @Override
  protected String getFixtureTestDataFolder() {
    return "resolve";
  }

  public void testExtendedClassConstantReferenceWithWildcard() {
    configure("""
                
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
                """);
    PsiReference reference = reference();
    PsiPolyVariantReference polyVariantReference = assertInstanceOf(reference, PsiPolyVariantReference.class);
    ResolveResult[] multiResolve = polyVariantReference.multiResolve(false);
    assertSize(2, multiResolve);
    Field first = assertInstanceOf(multiResolve[0].getElement(), Field.class);
    Field second = assertInstanceOf(multiResolve[1].getElement(), Field.class);
    assertEquals(Set.of("\\C.A_1", "\\C.A_2"), Set.of(first.getFQN(), second.getFQN()));
  }

  public void testExtendedClassConstantReferenceWithFullWildcard() {
    configure("""
                
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
                """);
    PsiReference reference = reference();
    PsiPolyVariantReference polyVariantReference = assertInstanceOf(reference, PsiPolyVariantReference.class);
    ResolveResult[] multiResolve = polyVariantReference.multiResolve(false);
    assertSize(3, multiResolve);
    Set<String> fqns = ContainerUtil.map2Set(multiResolve, it -> {
      return assertInstanceOf(it.getElement(), Field.class).getFQN();
    });
    assertEquals(Set.of("\\C.A_1", "\\C.A_2", "\\C.B_2"), fqns);
  }

  public void testResolveAliasReferenceToType() {
    configure("""
                
                <?php
                /**
                 * @psalm-type A_OR_B = A|B
                 * @psalm-type CoolType = <caret>A_OR_B|null
                 * @return CoolType
                 */
                function foo($a) {
                 \s
                }
                """);
    PsiElement resolved = resolve();
    assertEquals("A_OR_B", resolved.getText());
    assertEquals("@psalm-type A_OR_B = A|B", resolved.getParent().getText());
  }

  public void testResolveImportedAliasReferenceToType() {
    configure("""
                
                <?php
                /**
                 * @psalm-import-type FooAlias from Phone as MyFooAlias
                 * @psalm-type CoolType = <caret>MyFooAlias|null
                 */
                function foo($a) {
                 \s
                }
                """);
    PsiElement resolved = resolve();
    assertEquals("MyFooAlias", resolved.getText());
    assertEquals("@psalm-import-type FooAlias from Phone as MyFooAlias", resolved.getParent().getText());
  }

  public void testResolveImportedAliasFromMultilineReferenceToType() {
    configure("""
                
                <?php
                /**
                 * @psalm-import-type FooAlias from Phone as MyFooAlias
                 * @psalm-type FileMapType = array{
                 *      0: <caret>MyFooAlias
                 * }
                 */
                function foo($a) {
                 \s
                }
                """);
    PsiElement resolved = resolve();
    assertEquals("MyFooAlias", resolved.getText());
    assertEquals("@psalm-import-type FooAlias from Phone as MyFooAlias", resolved.getParent().getText());
  }

  public void testResolveImportedAliasFromPluralReferenceToType() {
    configure("""
                
                <?php
                /**
                 * @psalm-import-type FooAlias from Phone as MyFooAlias
                 * @psalm-type FileMapType = <caret>MyFooAlias[]
                 */
                function foo($a) {
                 \s
                }
                """);
    PsiElement resolved = resolve();
    assertEquals("MyFooAlias", resolved.getText());
    assertEquals("@psalm-import-type FooAlias from Phone as MyFooAlias", resolved.getParent().getText());
  }

  public void testResolveImportedFromStatement() {
    configure("""
                
                <?php
                /**
                 * @psalm-import-type FooAlias from Phone as MyFooAlias
                 */
                function foo($a) {
                    /** @var <caret>MyFooAlias $alias */
                    $phone = $param->toArray();
                }
                """);
    PsiElement resolved = resolve();
    assertEquals("MyFooAlias", resolved.getText());
    assertEquals("@psalm-import-type FooAlias from Phone as MyFooAlias", resolved.getParent().getText());
  }

  public void testResolveImportedFromStatementToContainingClass() {
    configure("""
                
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
                """);
    PsiElement resolved = resolve();
    assertEquals("MyFooAlias", resolved.getText());
    assertEquals("@psalm-import-type FooAlias from Phone as MyFooAlias", resolved.getParent().getText());
  }

  public void testResolveAliasToReferenceTypeFromClosure() {
    configure("""
                
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
                """);
    PsiElement resolved = resolve();
    assertEquals("MyFooAlias", resolved.getText());
    assertEquals("@psalm-import-type FooAlias from Phone as MyFooAlias", resolved.getParent().getText());
  }

  public void testResolveImportedFromMemberToContainingClass() {
    configure("""
                
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
                """);
    PsiElement resolved = resolve();
    assertEquals("MyFooAlias", resolved.getText());
    assertEquals("@psalm-import-type FooAlias from Phone as MyFooAlias", resolved.getParent().getText());
  }

  public void testResolveImportedFromClass() {
    configure("""
                
                <?php
                /**
                 * @psalm-import-type FooAlias from Phone as MyFooAlias
                 * @psalm-type TestType = array{base: <caret>MyFooAlias, description: string} \s
                 */
                class a {
                function foo($a) {
                }
                """);
    PsiElement resolved = resolve();
    assertEquals("MyFooAlias", resolved.getText());
    assertEquals("@psalm-import-type FooAlias from Phone as MyFooAlias", resolved.getParent().getText());
  }

  public void test_find_constant_by_special_self_class_name_in__see_tag() {
    configure("""
                
                <?php
                
                namespace {
                    /**
                     * @see <caret>self::foo()
                     */
                    class C {
                       function foo();
                    }
                }""");

    PhpClass phpClass = assertInstanceOf(resolve(), PhpClass.class);
    assertEquals("\\C", phpClass.getFQN());
  }

  public void test_find_constant_by_special_parent_class_name_in__see_tag() {
    configure("""
                
                <?php
                
                namespace {
                
                    class B {}
                    /**
                     * @see <caret>parent::foo()
                     */
                    class C extends B {
                       function foo();
                    }
                }""");

    PhpClass phpClass = assertInstanceOf(resolve(), PhpClass.class);
    assertEquals("\\B", phpClass.getFQN());
  }

  public void testGenericMixinMethod() {
    configureByFile();
    Method method = assertInstanceOf(resolve(), Method.class);
    assertEquals("\\MixinClass.mixinClassMethod", method.getFQN());
  }

  public void testGenericMixinField() {
    configureByFile();
    Field field = assertInstanceOf(resolve(), Field.class);
    assertEquals("\\MixinClass.$mixinClassProperty", field.getFQN());
  }

  public void testGenericMixinDecorator() {
    configureByFile();
    Method method = assertInstanceOf(resolve(), Method.class);
    assertEquals("\\ApiClient.request", method.getFQN());
  }

  public void testGenericMixinSeveralMixins() {
    configureByFile();
    Method method = assertInstanceOf(resolve(), Method.class);
    assertEquals("\\MixinClass2.mixinClassMethod2", method.getFQN());
  }

  public void testGenericMixinUnionTwoClassesWithMixins() {
    configureByFile();
    Method method = assertInstanceOf(resolve(), Method.class);
    assertEquals("\\MixinClass2.mixinClassMethod2", method.getFQN());
  }

  public void testGenericMixinGenericAndPlainMixins() {
    configureByFile();
    Method method = assertInstanceOf(resolve(), Method.class);
    assertEquals("\\MixinClass2.mixinClassMethod2", method.getFQN());
  }
}
