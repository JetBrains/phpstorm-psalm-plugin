package com.jetbrains.php.psalm.lang;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.testFramework.junit5.TestApplication;
import com.intellij.testFramework.junit5.fixture.TestFixture;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.fixtures.PhpCodeInsightTestFixture;
import com.jetbrains.php.fixtures.PhpTestDataLayout;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.psalm.types.PsalmTypeInferenceTest;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.jetbrains.php.fixtures.PhpCodeInsightTestFixture.phpCodeInsightFixture;
import static com.jetbrains.php.fixtures.PhpTestFixtures.phpProjectFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@TestApplication
public class PsalmResolveTest {
  private static final TestFixture<Project> projectFixture = phpProjectFixture();
  private final TestFixture<PhpCodeInsightTestFixture> php =
    phpCodeInsightFixture(projectFixture, new PhpTestDataLayout("resolve").withHome(PsalmTypeInferenceTest.TEST_DATA_HOME));

  @Test
  public void testExtendedClassConstantReferenceWithWildcard() {
    php.get().configure("""
                
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
    PsiReference reference = php.get().reference();
    ReadAction.runBlocking(() -> {
      PsiPolyVariantReference polyVariantReference = assertInstanceOf(PsiPolyVariantReference.class, reference);
      ResolveResult[] multiResolve = polyVariantReference.multiResolve(false);
      assertEquals(2, multiResolve.length);
      Field first = assertInstanceOf(Field.class, multiResolve[0].getElement());
      Field second = assertInstanceOf(Field.class, multiResolve[1].getElement());
      assertEquals(Set.of("\\C.A_1", "\\C.A_2"), Set.of(first.getFQN(), second.getFQN()));
    });
  }

  @Test
  public void testExtendedClassConstantReferenceWithFullWildcard() {
    php.get().configure("""
                
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
    PsiReference reference = php.get().reference();
    ReadAction.runBlocking(() -> {
      PsiPolyVariantReference polyVariantReference = assertInstanceOf(PsiPolyVariantReference.class, reference);
      ResolveResult[] multiResolve = polyVariantReference.multiResolve(false);
      assertEquals(3, multiResolve.length);
      Set<String> fqns = ContainerUtil.map2Set(multiResolve, it -> {
        return assertInstanceOf(Field.class, it.getElement()).getFQN();
      });
      assertEquals(Set.of("\\C.A_1", "\\C.A_2", "\\C.B_2"), fqns);
    });
  }

  @Test
  public void testResolveAliasReferenceToType() {
    php.get().configure("""
                
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
    PsiElement resolved = php.get().resolve();
    ReadAction.runBlocking(() -> {
      assertEquals("A_OR_B", resolved.getText());
      assertEquals("@psalm-type A_OR_B = A|B", resolved.getParent().getText());
    });
  }

  @Test
  public void testResolveImportedAliasReferenceToType() {
    php.get().configure("""
                
                <?php
                /**
                 * @psalm-import-type FooAlias from Phone as MyFooAlias
                 * @psalm-type CoolType = <caret>MyFooAlias|null
                 */
                function foo($a) {
                 \s
                }
                """);
    PsiElement resolved = php.get().resolve();
    ReadAction.runBlocking(() -> {
      assertEquals("MyFooAlias", resolved.getText());
      assertEquals("@psalm-import-type FooAlias from Phone as MyFooAlias", resolved.getParent().getText());
    });
  }

  @Test
  public void testResolveImportedAliasFromMultilineReferenceToType() {
    php.get().configure("""
                
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
    PsiElement resolved = php.get().resolve();
    ReadAction.runBlocking(() -> {
      assertEquals("MyFooAlias", resolved.getText());
      assertEquals("@psalm-import-type FooAlias from Phone as MyFooAlias", resolved.getParent().getText());
    });
  }

  @Test
  public void testResolveImportedAliasFromPluralReferenceToType() {
    php.get().configure("""
                
                <?php
                /**
                 * @psalm-import-type FooAlias from Phone as MyFooAlias
                 * @psalm-type FileMapType = <caret>MyFooAlias[]
                 */
                function foo($a) {
                 \s
                }
                """);
    PsiElement resolved = php.get().resolve();
    ReadAction.runBlocking(() -> {
      assertEquals("MyFooAlias", resolved.getText());
      assertEquals("@psalm-import-type FooAlias from Phone as MyFooAlias", resolved.getParent().getText());
    });
  }

  @Test
  public void testResolveImportedFromStatement() {
    php.get().configure("""
                
                <?php
                /**
                 * @psalm-import-type FooAlias from Phone as MyFooAlias
                 */
                function foo($a) {
                    /** @var <caret>MyFooAlias $alias */
                    $phone = $param->toArray();
                }
                """);
    PsiElement resolved = php.get().resolve();
    ReadAction.runBlocking(() -> {
      assertEquals("MyFooAlias", resolved.getText());
      assertEquals("@psalm-import-type FooAlias from Phone as MyFooAlias", resolved.getParent().getText());
    });
  }

  @Test
  public void testResolveImportedFromStatementToContainingClass() {
    php.get().configure("""
                
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
    PsiElement resolved = php.get().resolve();
    ReadAction.runBlocking(() -> {
      assertEquals("MyFooAlias", resolved.getText());
      assertEquals("@psalm-import-type FooAlias from Phone as MyFooAlias", resolved.getParent().getText());
    });
  }

  @Test
  public void testResolveAliasToReferenceTypeFromClosure() {
    php.get().configure("""
                
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
    PsiElement resolved = php.get().resolve();
    ReadAction.runBlocking(() -> {
      assertEquals("MyFooAlias", resolved.getText());
      assertEquals("@psalm-import-type FooAlias from Phone as MyFooAlias", resolved.getParent().getText());
    });
  }

  @Test
  public void testResolveImportedFromMemberToContainingClass() {
    php.get().configure("""
                
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
    PsiElement resolved = php.get().resolve();
    ReadAction.runBlocking(() -> {
      assertEquals("MyFooAlias", resolved.getText());
      assertEquals("@psalm-import-type FooAlias from Phone as MyFooAlias", resolved.getParent().getText());
    });
  }

  @Test
  public void testResolveImportedFromClass() {
    php.get().configure("""
                
                <?php
                /**
                 * @psalm-import-type FooAlias from Phone as MyFooAlias
                 * @psalm-type TestType = array{base: <caret>MyFooAlias, description: string} \s
                 */
                class a {
                function foo($a) {
                }
                """);
    PsiElement resolved = php.get().resolve();
    ReadAction.runBlocking(() -> {
      assertEquals("MyFooAlias", resolved.getText());
      assertEquals("@psalm-import-type FooAlias from Phone as MyFooAlias", resolved.getParent().getText());
    });
  }

  @Test
  public void test_find_constant_by_special_self_class_name_in__see_tag() {
    php.get().configure("""
                
                <?php
                
                namespace {
                    /**
                     * @see <caret>self::foo()
                     */
                    class C {
                       function foo();
                    }
                }""");

    PhpClass phpClass = assertInstanceOf(PhpClass.class, php.get().resolve());
    ReadAction.runBlocking(() -> {
      assertEquals("\\C", phpClass.getFQN());
    });
  }

  @Test
  public void test_find_constant_by_special_parent_class_name_in__see_tag() {
    php.get().configure("""
                
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

    PhpClass phpClass = assertInstanceOf(PhpClass.class, php.get().resolve());
    ReadAction.runBlocking(() -> {
      assertEquals("\\B", phpClass.getFQN());
    });
  }

  @Test
  public void testGenericMixinMethod() {
    php.get().configureByFile();
    Method method = assertInstanceOf(Method.class, php.get().resolve());
    ReadAction.runBlocking(() -> {
      assertEquals("\\MixinClass.mixinClassMethod", method.getFQN());
    });
  }

  @Test
  public void testGenericMixinField() {
    php.get().configureByFile();
    Field field = assertInstanceOf(Field.class, php.get().resolve());
    ReadAction.runBlocking(() -> {
      assertEquals("\\MixinClass.$mixinClassProperty", field.getFQN());
    });
  }

  @Test
  public void testGenericMixinDecorator() {
    php.get().configureByFile();
    Method method = assertInstanceOf(Method.class, php.get().resolve());
    ReadAction.runBlocking(() -> {
      assertEquals("\\ApiClient.request", method.getFQN());
    });
  }

  @Test
  public void testGenericMixinSeveralMixins() {
    php.get().configureByFile();
    Method method = assertInstanceOf(Method.class, php.get().resolve());
    ReadAction.runBlocking(() -> {
      assertEquals("\\MixinClass2.mixinClassMethod2", method.getFQN());
    });
  }

  @Test
  public void testGenericMixinUnionTwoClassesWithMixins() {
    php.get().configureByFile();
    Method method = assertInstanceOf(Method.class, php.get().resolve());
    ReadAction.runBlocking(() -> {
      assertEquals("\\MixinClass2.mixinClassMethod2", method.getFQN());
    });
  }

  @Test
  public void testGenericMixinGenericAndPlainMixins() {
    php.get().configureByFile();
    Method method = assertInstanceOf(Method.class, php.get().resolve());
    ReadAction.runBlocking(() -> {
      assertEquals("\\MixinClass2.mixinClassMethod2", method.getFQN());
    });
  }
}
