package com.jetbrains.php.psalm.types;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.project.Project;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;
import com.intellij.testFramework.junit5.TestApplication;
import com.intellij.testFramework.junit5.fixture.TestFixture;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.codeInsight.PhpTypeInferenceTestFixture;
import com.jetbrains.php.config.PhpLanguageLevel;
import com.jetbrains.php.fixtures.PhpTestDataLayout;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.Variable;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import org.junit.jupiter.api.Test;

import static com.jetbrains.php.codeInsight.PhpTypeInferenceTestFixture.phpTypeInferenceFixture;
import static com.jetbrains.php.fixtures.PhpCodeInsightTestFixture.doLanguageLevelTest;
import static com.jetbrains.php.fixtures.PhpTestFixtures.phpProjectFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestApplication
public class PsalmTypeInferenceTest {
  private static final TestFixture<Project> projectFixture = phpProjectFixture();
  private final TestFixture<PhpTypeInferenceTestFixture> php =
    phpTypeInferenceFixture(projectFixture, new PhpTestDataLayout("codeInsight/typeInference").withHome(TEST_DATA_HOME));

  public static final String TEST_DATA_HOME = "/phpstorm/psalm/testData/";



  @Test
  public void testGenericArray$simpleKey() {
    php.get().doTypeTest();
  }

  @Test
  public void testGenericArray$simpleValue() {
    php.get().doTypeTest();
  }

  @Test
  public void testGenericArray$nonEmptyArray() {
    php.get().doTypeTest();
  }

  @Test
  public void testGenericArray$nestedKey() {
    php.get().doTypeTest();
  }

  @Test
  public void testGenericArray$nestedValue() {
    php.get().doTypeTest();
  }

  @Test
  public void testGenericArray$nestedValueWithKey() {
    php.get().doTypeTest();
  }

  @Test
  public void testGenericArray$union() {
    php.get().doTypeTest();
  }

  @Test
  public void testGenericArray$nonArray() {
    php.get().doTypeTest(); // no particular guarantees here
  }

  @Test
  public void testGenericArray$nestedClassString() {
    php.get().doTypeTest();
  }

  @Test
  public void testPsalmParam() {
    php.get().doTypeTest();
  }

  @Test
  public void testPsalmReturn() {
    php.get().doTypeTest();
  }

  @Test
  public void testPsalmReturnGeneric() {
    php.get().doTypeTest();
  }

  @Test
  public void testPsalmVarField() {
    php.get().doTypeTest();
  }

  @Test
  public void testPsalmVarVar() {
    php.get().doTypeTest();
  }

  @Test
  public void testClassString() {
    php.get().doTypeTest();
  }

  @Test
  public void testCallableString() {
    php.get().doTypeTest();
  }

  @Test
  public void testNumericString() {
    php.get().doTypeTest();
  }

  @Test
  public void testLowercaseString() {
    php.get().doTypeTest();
  }

  @Test
  public void testNonEmptyLowercaseString() {
    php.get().doTypeTest();
  }

  @Test
  public void testTruthyString() {
    php.get().doTypeTest();
  }

  @Test
  public void testNonFalsyString() {
    php.get().doTypeTest();
  }

  @Test
  public void testTraitString() {
    php.get().doTypeTest();
  }

  @Test
  public void testScalar() {
    php.get().doTypeTest();
  }

  @Test
  public void testNumeric() {
    php.get().doTypeTest();
  }

  @Test
  public void testIntMask() {
    php.get().doTypeTest();
  }

  @Test
  public void testIntMaskOf() {
    php.get().doTypeTest();
  }

  @Test
  public void testArrayKey() {
    php.get().doTypeTest();
  }

  @Test
  public void testArrayKeyTemplateType() {
    doLanguageLevelTest(php.get().getProject(), PhpLanguageLevel.PHP850, () -> {
      php.get().doTypeTest();
    });
  }

  @Test
  public void testArrayKeyVarType() {
    doLanguageLevelTest(php.get().getProject(), PhpLanguageLevel.PHP850, () -> {
      php.get().doTypeTest();
    });
  }

  @Test
  public void testTypesFromExtendedClassConstants() {
    php.get().doTypeTest();
  }

  @Test
  public void testTypesFromExtendedClassConstantsWildcard() {
    php.get().doTypeTest();
  }

  @Test
  public void testFunctionTemplate() {
    php.get().doTypeTest();
  }

  @Test
  public void testMethodTemplate() {
    php.get().doTypeTest();
  }

  @Test
  public void testClassTemplate() {
    php.get().doTypeTest();
  }

  @Test
  public void testPsalmTagsTemplate() {
    php.get().doTypeTest();
  }

  @Test
  public void testWithoutTemplate() {
    php.get().doTypeTest();
  }

  @Test
  public void testClosure() {
    php.get().doTypeTest();
  }

  @Test
  public void testIntArrayKey() {
    php.get().doTypeTest();
  }

  @Test
  public void testListArrayKey() {
    php.get().doTypeTest();
  }

  @Test
  public void testStringArrayKey() {
    php.get().doTypeTest();
  }

  @Test
  public void testUnknownArrayKey() {
    php.get().doTypeTest();
  }

  @Test
  public void testScalarGeneric() {
    php.get().doTypeTest();
  }

  @Test
  public void testPluralClassStringWithArgument() {
    php.get().doTypeTest();
  }

  @Test
  public void testPluralClassString() {
    php.get().doTypeTest();
  }

  @Test
  public void testExtendedTypes() {
    php.get().doTypeTest();
  }

  @Test
  public void testNonEmptyList() {
    php.get().doTypeTest();
  }

  @Test
  public void testNonEmptyListWithoutTypes() {
    php.get().doTypeTest();
  }

  @Test
  public void testNonEmptyString() {
    php.get().doTypeTest();
  }

  @Test
  public void testIterableKey() {
    php.get().doTypeTest();
  }

  @Test
  public void testAdvancedCallable() {
    php.get().getFixture().addFileToProject("a.php", """
      <?php
      class Foo {}
      /**
       * @return Closure(bool, int|string, $a int) : (int|Foo)
       */
      function a(): Closure{

      }

      function b(){
          return a();
      }""");
    php.get().doTypeTest();
  }

  @Test
  public void testAdvancedCallableInSameFile() {
    php.get().doTypeTest();
  }

  @Test
  public void testAdvancedCallableImported() {
    php.get().doTypeTest();
  }

  @Test
  public void testAdvancedCallableUnionType() {
    php.get().doTypeTest();
  }

  @Test
  public void testAdvancedCallableKeyword() {
    php.get().doTypeTest();
  }

  @Test
  public void testElementTypeOfArrayKey() {
    php.get().doTypeTest();
  }

  @Test
  public void testArrayKeyOfMultiDimensionalArray() {
    php.get().doTypeTest();
  }

  @Test
  public void testStubsConsistencyFunction() {
    php.get().getFixture().addFileToProject("a.php", """
      <?php

      /**
       * @psalm-return int
       */
      function f(): string
      {
      }""");
    ReadAction.runBlocking(() -> {
      Function function = PhpIndex.getInstance(php.get().getProject()).getFunctionsByFQN("\\f").iterator().next();
      assertEquals(new PhpType().add(PhpType.INT).add(PhpType.STRING), function.getGlobalType());
    });
    php.get().doTypeTest();
  }

  @Test
  public void testStubsConsistencyParameter() {
    php.get().getFixture().addFileToProject("a.php", """
      <?php

      /**
       * @psalm-param int $a
       */
      function f(string $a)
      {
      }""");
    ReadAction.runBlocking(() -> {
      Parameter param = PhpIndex.getInstance(php.get().getProject()).getFunctionsByFQN("\\f").iterator().next().getParameter(0);
      assertEquals(new PhpType().add(PhpType.INT).add(PhpType.STRING), param.getGlobalType());
    });
    php.get().doTypeTest();
  }

  @Test
  public void testStubsConsistencyField() {
    php.get().getFixture().addFileToProject("a.php", """
      <?php

      class A {
          /**
           * @psalm-var int $f
           */
          public string $f;
      }""");
    ReadAction.runBlocking(() -> {
      Field field = PhpIndex.getInstance(php.get().getProject()).getClassesByFQN("\\A").iterator().next().getOwnFields()[0];
      assertEquals(new PhpType().add(PhpType.INT).add(PhpType.STRING), field.getGlobalType());
    });
    php.get().doTypeTest();
  }

  @Test
  public void testStubsConsistencyVariable() {
    php.get().getFixture().addFileToProject("a.php", """
      <?php

      /**
       * @psalm-var int $vvv
       */
      $vvv ='a';""");
    ReadAction.runBlocking(() -> {
      Variable variable = PhpIndex.getInstance(php.get().getProject()).getVariablesByName("vvv").iterator().next();
      assertEquals(new PhpType().add(PhpType.INT), variable.getGlobalType());
    });
    php.get().doTypeTest();
  }

  @Test
  public void testPsalmVarOnCorrectVariable() {
    php.get().doTypeTest(true);
  }

  @Test
  public void testPsalmArrayShape() {
    php.get().doTypeTest(true);
  }

  @Test
  public void testPsalmMultilineArrayShape() {
    php.get().doTypeTest(true);
  }

  @Test
  public void testPsalmArrayShapeMultipleFiles() {
    CodeInsightTestFixture myFixture = php.get().getFixture();
    myFixture.addFileToProject("aa.php", """
      <?php
      /**
       * @psalm-return array{age?: Exception, f: int}
       */
      function f(){}""");
    myFixture.addFileToProject("b.php", """
      <?php

      function ff() {
          return f();
      }""");
    myFixture.addFileToProject("c.php", """
      <?php
      /**
       * @psalm-return array<array{age?: Exception, f: int}>
       */
      function ff1(){}""");
    myFixture.addFileToProject("d.php", """
      <?php
      class AA {
      /**
       * @psalm-return array{age?: Exception, f: int}
       */
      function ff1(){}
      }""");

    php.get().doTypeTest(true);
  }

  @Test
  public void testTemplateClassString() {
    php.get().doTypeTest();
  }

  @Test
  public void testTemplateExtends() {
    php.get().doTypeTest();
  }

  @Test
  public void testTemplateExtendsNamespace() {
    php.get().doTypeTest();
  }

  @Test
  public void testTemplateExtendsMultipleTemplates() {
    php.get().doTypeTest();
  }

  @Test
  public void testTemplateImplements() {
    php.get().doTypeTest();
  }

  @Test
  public void testReturnTemplatedClass() {
    php.get().doTypeTest();
  }

  @Test
  public void testTemplatedClassVariableDocType() {
    php.get().doTypeTest();
  }

  @Test
  public void testReturnTemplatedClassNamespace() {
    php.get().doTypeTest();
  }

  @Test
  public void testLocalClassString() {
    php.get().doTypeTest();
  }

  @Test
  public void testLocalClassStringNamespace() {
    doLanguageLevelTest(php.get().getProject(), PhpLanguageLevel.PHP800, () -> php.get().doTypeTest());
  }

  @Test
  public void testLocalTypeUnwrappingSameFile() {
    php.get().doTypeTest();
  }

  @Test
  public void testLocalTypeUnwrappingDifferentFile() {
    php.get().addPhpFileToProject("a.php", """
      <?php
      class C
      {
      }

      /**
       * @template T
       */
      class Bar
      {
          /**
           * @psalm-return T
           */
          public function doBaz()
          {
          }
      }
      """);
    php.get().doTypeTest();
  }

  @Test
  public void testTemplatedIteratorInForeach() {
    php.get().doTypeTest();
  }

  @Test
  public void testExtendedClassesWithFallthroughTemplates() {
    php.get().doTypeTest();
  }

  @Test
  public void testExtendedClassesWithFallthroughTemplatesNamespaced() {
    php.get().doTypeTest();
  }

  @Test
  public void testGenericsConstructorInference() {
    php.get().doTypeTest();
  }

  @Test
  public void testMultipleTags() {
    php.get().doTypeTest();
  }

  @Test
  public void testSuperMember() {
    php.get().doTypeTest();
  }

  @Test
  public void testSuperMemberRecursive() {
    php.get().doTypeTest();
  }

  @Test
  public void testSubstituteExtendedClasses() {
    php.get().doTypeTest();
  }

  @Test
  public void testTemplateExtendsByPassing() {
    php.get().doTypeTest();
  }

  @Test
  public void testTemplateExtendsByPassingStaticReturn() {
    php.get().doTypeTest();
  }

  @Test
  public void testTemplateWrappedArray() {
    php.get().doTypeTest();
  }
  @Test
  public void testTemplateUnwrappedArray() {
    php.get().doTypeTest();
  }

  @Test
  public void testTemplateUnwrappedArrayConstructorCall() {
    php.get().doTypeTest();
  }

  @Test
  public void testTemplateUnwrappedArrayConstructorCallNonArrayPassed() {
    php.get().doTypeTest();
  }

  @Test
  public void testTemplateWrappedClass() {
    php.get().doTypeTest();
  }

  @Test
  public void testParameterUnwrap() {
    php.get().doTypeTest();
  }

  @Test
  public void testIssues$wi85100DryRunRegressions() {
    php.get().doTypeTest();
  }

  @Test
  public void testParameterUnwrapToArray() {
    php.get().doTypeTest();
  }

  @Test
  public void testParameterUnwrapToStaticGeneric() {
    php.get().doTypeTest();
  }

  @Test
  public void testClassNamesFromClassStringDocVar() {
    php.get().doTypeTest();
  }

  @Test
  public void testArrayShapePlural() {
    php.get().doTypeTest();
  }

  @Test
  public void testArrayShapeNumeric() {
    php.get().doTypeTest();
  }

  @Test
  public void testGenericDocMethod() {
    php.get().doTypeTest();
  }

  @Test
  public void testArrayDocMethod() {
    php.get().doTypeTest();
  }

  @Test
  public void testTypeAlias() {
    php.get().doTypeTest();
  }

  @Test
  public void testTypeAliasGlobal() {
    php.get().getFixture().addFileToProject("aa.php", """
      <?php

      class Foo {}
      class Bar {}
      /**
       * @psalm-type FooAlias = Foo
       * @psalm-type BarAlias = Bar
       */
      class Phone {

      }""");
    php.get().doTypeTest();
  }

  @Test
  public void testTypeAliasPlural() {
    php.get().doTypeTest();
  }

  @Test
  public void testInferredVarParameter() {
    php.get().doTypeTest();
  }

  @Test
  public void testNestedListInMultilineDoc() {
    php.get().doTypeTest();
  }

  @Test
  public void testGenericYield() {
    php.get().doTypeTest();
  }

  @Test
  public void testGenericYieldDifferentFile() {
    php.get().addPhpFileToProject("a.php", """
      <?php
      /**
       * @template-covariant TValue
       * @psalm-yield TValue
       */
      interface Promise
      {
          /**
           * @return TValue
           */
          function f();
      }

      class Foo{}

      /**
       * @return Promise<Foo>
       */
      function f(){}
      class Bar{}

      /**
       * @template TKey
       * @template TValue
       * @psalm-yield TValue
       */
      class Holder {
      }
      class Base {
           /**
           * @var Holder<Foo, Bar>
           */
          private $a;
      }""");
    php.get().doTypeTest();
  }

  @Test
  public void testGenericIteratorBases() {
    php.get().doTypeTest();
  }

  @Test
  public void testGenericIteratorSubstitutionByImplementedMethod() {
    php.get().doTypeTest();
  }

  @Test
  public void testGenericPassingParameter() {
    php.get().doTypeTest();
  }

  @Test
  public void testGenericPassingParameterDifferentFile() {
    php.get().addPhpFileToProject("a.php", """
      <?php
      /**
       * @template T
       */
      class C1{
          /**
           * @return C2<T>
           */
          public function f(){}
      }
      /**
       * @template T
       */
      class C2{
          /**
           * @return T
           */
          public function f(){}

      }""");
    php.get().doTypeTest();
  }

  @Test
  public void testGenericPassingParameterStaticDifferentFile() {
    php.get().addPhpFileToProject("a.php", """
      <?php
      /**
       * @template TValue
       */
      class C {
          /**
           * @return static<array<TValue>>
           */
          public function crossJoin($lists) { }
          /**
           * @return TValue
           */
          public function first() { }
      }""");
    php.get().doTypeTest();
  }

  @Test
  public void testGenericPassingParameterStatic() {
    php.get().doTypeTest();
  }

  @Test
  public void testGenericIterableUnwrap() {
    php.get().doTypeTest();
  }

  @Test
  public void testGenericIterableUnwrapDifferentFile() {
    php.get().addPhpFileToProject("a.php", """
      <?php
      /**
       * @template T
       */
      class C {
          /**
           * @template TValue
           * @param  iterable<TValue>  $param
           * @return TValue
           */
          public function f($param) {
          }
      }""");
    php.get().doTypeTest();
  }

  @Test
  public void testElementTypeOfGenericClassDifferentFile() {
    php.get().addPhpFileToProject("a.php", """
      <?php

      /**
       * @template TKey of array-key
       * @template TValue
       */
      class B {
          /**
           * @return array<TKey, TValue>
           */
          public function all()
          {

          }
      }

      /**
       * @template TKey of array-key
       * @template TValue
       */
      class A {
          /**
           * @return B<TKey, TValue>
           */
          public function lazy()
          {

          }
      }""");
    php.get().doTypeTest();
  }

  @Test
  public void testElementTypeOfGenericClass() {
    php.get().doTypeTest();
  }

  @Test
  public void testGenericFlipClassParameters() {
    php.get().doTypeTest();
  }

  @Test
  public void testGenericPassingParameterMultipleSubstitutions() {
    php.get().doTypeTest();
  }

  @Test
  public void testInnerParametrizedParts() {
    // no exception
    php.get().doTypeTest();
  }

  @Test
  public void testAdvancedCallableInferred() {
    php.get().doTypeTest();
  }

  @Test
  public void testAdvancedCallableDoc() {
    php.get().doTypeTest();
  }

  @Test
  public void testAdvancedCallableMetadata() {
    php.get().doTypeTestWithParameterisedTypes();
  }

  @Test
  public void testAdvancedCallableMethodRef() {
    php.get().addPhpFileToProject("a.php", """
      <?php
      namespace X;
      /**
       * @template T
       * @template T1
       * @param T $a
       * @param T1 $a1
       * @return AA<T, T1>
       */
      function collect($a, $a1) {}""");
    php.get().doTypeTest();
  }

  @Test
  public void testAdvancedCallableMethodRefCallable() {
    php.get().doTypeTest();
  }

  @Test
  public void testTemplatedArrayKey() {
    php.get().doTypeTest();
  }

  @Test
  public void testDocTypesInParentheses() {
    php.get().doTypeTest();
  }

  @Test
  public void testPsalmScalarTypeArrayKey() {
    php.get().doTypeTest();
  }

  @Test
  public void testPsalmNotNumericArrayKey() {
    php.get().doTypeTest();
  }

  @Test
  public void testArrayShapeMultiAssignmentOmittedElements() {
    php.get().doTypeTest();
  }

  @Test
  public void testVarOnPromotedProperty() {
    php.get().doTypeTest();
  }

  @Test
  public void testVarOnPromotedPropertyWithDocCommentOnMethod() {
    php.get().doTypeTest();
  }

  @Test
  public void testParametrisedPolymorphicCallName() {
    php.get().addPhpFileToProject("a.php", """
      <?php

      class Two {
          public static function getOne(): One {}
      }
      """);
    php.get().doTypeTest();
  }

  @Test
  public void testKeyOfValueOf() {
    php.get().doTypeTest();
  }

  @Test
  public void testValueOfBackedEnumInArray() {
    doLanguageLevelTest(php.get().getProject(), PhpLanguageLevel.PHP810, () -> php.get().doTypeTest());
  }

  @Test
  public void testValueOfBackedEnumInArrayShorthand() {
    doLanguageLevelTest(php.get().getProject(), PhpLanguageLevel.PHP810, () -> php.get().doTypeTest());
  }

  @Test
  public void testFetchExtendsThroughIntermediateValue() {
    php.get().doTypeTest();
  }

  @Test
  public void testFetchExtendsFromParentClass() {
    php.get().doTypeTest();
  }

  @Test
  public void testExpandFallthroughTemplatesInConstructor() {
    php.get().doTypeTest();
  }

  @Test
  public void testLiteralString() {
    php.get().doTypeTest();
  }

  @Test
  public void testMetaFromSuperParameter() {
    php.get().doTypeTest();
  }

  @Test
  public void testMetaFromSuperParameterGlobal() {
    php.get().addPhpFileToProject("a.php", """
      <?php
      /**
       * @template T
       */
      interface I
      {
          /**
           * @param A $min
           * @param B $max
           * @return T
           */
          public function rand($min, $max);
      }""");
    php.get().doTypeTest();
  }

  @Test
  public void testMultipleInheritanceWithExtends() {
    php.get().doTypeTest();
  }

  @Test
  public void testLiteralType() {
    php.get().doTypeTest();
  }

  @Test
  public void testNegativeInt() {
    php.get().doTypeTest();
  }

  @Test
  public void testNonNegativeInt() {
    php.get().doTypeTest();
  }

  @Test
  public void testNonPositiveInt() {
    php.get().doTypeTest();
  }

  @Test
  public void testGenericClassStringParameterWithSuperTypeForT() {
    php.get().doTypeTest();
  }

  @Test
  public void testGenericClassStringConstructor() {
    php.get().doTypeTest();
  }

  @Test
  public void testGenericClassStringConstructorWithPromotedProperty() {
    php.get().doTypeTest();
  }

  @Test
  public void testGenericClassStringConstructorWithPromotedPropertyWithParamTag() {
    php.get().doTypeTest();
  }

  @Test
  public void testDocMethodReturnsGenericList() {
    php.get().doTypeTest(true);
  }

  @Test
  public void testDocMethodReturnsClassTemplateList() {
    php.get().doTypeTest(true);
  }

  @Test
  public void testGenericAndStatic$static() {
    php.get().doTypeTest(true);
  }

  @Test
  public void testGenericAndStatic$staticWithEntities() {
    php.get().doTypeTest(true);
  }

  @Test
  public void testGenericAndStatic$this() {
    php.get().doTypeTest(true);
  }

  @Test
  public void testGenericAndStatic$arrayObject() {
    php.get().doTypeTest(true);
  }

  @Test
  public void testGenericAndStatic$queryBuilder() {
    php.get().doTypeTest(true);
  }

  @Test
  public void testReturnStaticOrThis$this() {
    php.get().doTypeTest(true);
  }

  @Test
  public void testReturnStaticOrThis$thisWithForeach() {
    php.get().doTypeTest(true);
  }

  @Test
  public void testReturnStaticOrThis$arrayOfThis() {
    php.get().doTypeTest(true);
  }

  @Test
  public void testReturnStaticOrThis$static() {
    php.get().doTypeTest(true);
  }

  @Test
  public void testLocalTypeUnwrappingTagOrderMustNotMatter() {
    php.get().doTypeTest(true);
  }

  @Test
  public void testNewExpressionWithClassString() {
    php.get().doTypeTest(true);
  }

  @Test
  public void testClassStringConstructorParameterTypeStaticCall() {
    php.get().doTypeTest(true);
  }

  @Test
  public void testFloatType() {
    php.get().doTypeTest(true);
  }
}
