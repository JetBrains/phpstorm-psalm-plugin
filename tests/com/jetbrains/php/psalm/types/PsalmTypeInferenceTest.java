package com.jetbrains.php.psalm.types;

import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.codeInsight.PhpTypeInferenceTestCase;
import com.jetbrains.php.config.PhpLanguageLevel;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.Variable;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import org.jetbrains.annotations.NotNull;

public class PsalmTypeInferenceTest extends PhpTypeInferenceTestCase {
  public static final String TEST_DATA_HOME = "/phpstorm/psalm/testData/";

  @Override
  protected @NotNull String getTestDataHome() {
    return TEST_DATA_HOME;
  }

  @Override
  protected String getFixtureTestDataFolder() {
    return "codeInsight/typeInference";
  }

  public void testGenericArray$simpleKey() {
    doTypeTest();
  }

  public void testGenericArray$simpleValue() {
    doTypeTest();
  }

  public void testGenericArray$nonEmptyArray() {
    doTypeTest();
  }

  public void testGenericArray$nestedKey() {
    doTypeTest();
  }

  public void testGenericArray$nestedValue() {
    doTypeTest();
  }

  public void testGenericArray$nestedValueWithKey() {
    doTypeTest();
  }

  public void testGenericArray$union() {
    doTypeTest();
  }

  public void testGenericArray$nonArray() {
    doTypeTest(); // no particular guarantees here
  }

  public void testGenericArray$nestedClassString() {
    doTypeTest();
  }

  public void testPsalmParam() {
    doTypeTest();
  }

  public void testPsalmReturn() {
    doTypeTest();
  }

  public void testPsalmReturnGeneric() {
    doTypeTest();
  }

  public void testPsalmVarField() {
    doTypeTest();
  }

  public void testPsalmVarVar() {
    doTypeTest();
  }

  public void testClassString() {
    doTypeTest();
  }

  public void testCallableString() {
    doTypeTest();
  }

  public void testNumericString() {
    doTypeTest();
  }

  public void testLowercaseString() {
    doTypeTest();
  }

  public void testNonEmptyLowercaseString() {
    doTypeTest();
  }

  public void testTruthyString() {
    doTypeTest();
  }

  public void testNonFalsyString() {
    doTypeTest();
  }

  public void testTraitString() {
    doTypeTest();
  }

  public void testScalar() {
    doTypeTest();
  }

  public void testNumeric() {
    doTypeTest();
  }

  public void testIntMask() {
    doTypeTest();
  }

  public void testIntMaskOf() {
    doTypeTest();
  }

  public void testArrayKey() {
    doTypeTest();
  }

  public void testTypesFromExtendedClassConstants() {
    doTypeTest();
  }

  public void testTypesFromExtendedClassConstantsWildcard() {
    doTypeTest();
  }

  public void testFunctionTemplate() {
    doTypeTest();
  }

  public void testMethodTemplate() {
    doTypeTest();
  }

  public void testClassTemplate() {
    doTypeTest();
  }

  public void testPsalmTagsTemplate() {
    doTypeTest();
  }

  public void testWithoutTemplate() {
    doTypeTest();
  }

  public void testClosure() {
    doTypeTest();
  }

  public void testIntArrayKey() {
    doTypeTest();
  }

  public void testListArrayKey() {
    doTypeTest();
  }

  public void testStringArrayKey() {
    doTypeTest();
  }

  public void testUnknownArrayKey() {
    doTypeTest();
  }

  public void testScalarGeneric() {
    doTypeTest();
  }

  public void testPluralClassStringWithArgument() {
    doTypeTest();
  }

  public void testPluralClassString() {
    doTypeTest();
  }

  public void testExtendedTypes() {
    doTypeTest();
  }

  public void testNonEmptyList() {
    doTypeTest();
  }

  public void testNonEmptyListWithoutTypes() {
    doTypeTest();
  }

  public void testNonEmptyString() {
    doTypeTest();
  }

  public void testIterableKey() {
    doTypeTest();
  }

  public void testAdvancedCallable() {
    myFixture.addFileToProject("a.php", """
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
    doTypeTest();
  }

  public void testAdvancedCallableInSameFile() {
    doTypeTest();
  }

  public void testAdvancedCallableImported() {
    doTypeTest();
  }

  public void testAdvancedCallableUnionType() {
    doTypeTest();
  }

  public void testAdvancedCallableKeyword() {
    doTypeTest();
  }

  public void testElementTypeOfArrayKey() {
    doTypeTest();
  }

  public void testArrayKeyOfMultiDimensionalArray() {
    doTypeTest();
  }

  public void testStubsConsistencyFunction() {
    myFixture.addFileToProject("a.php", """
      <?php

      /**
       * @psalm-return int
       */
      function f(): string
      {
      }""");
    Function function = PhpIndex.getInstance(getProject()).getFunctionsByFQN("\\f").iterator().next();
    assertEquals(new PhpType().add(PhpType.INT).add(PhpType.STRING), function.getGlobalType());
    doTypeTest();
  }

  public void testStubsConsistencyParameter() {
    myFixture.addFileToProject("a.php", """
      <?php

      /**
       * @psalm-param int $a
       */
      function f(string $a)
      {
      }""");
    Parameter param = PhpIndex.getInstance(getProject()).getFunctionsByFQN("\\f").iterator().next().getParameter(0);
    assertEquals(new PhpType().add(PhpType.INT).add(PhpType.STRING), param.getGlobalType());
    doTypeTest();
  }

  public void testStubsConsistencyField() {
    myFixture.addFileToProject("a.php", """
      <?php

      class A {
          /**
           * @psalm-var int $f
           */
          public string $f;
      }""");
    Field field = PhpIndex.getInstance(getProject()).getClassesByFQN("\\A").iterator().next().getOwnFields()[0];
    assertEquals(new PhpType().add(PhpType.INT).add(PhpType.STRING), field.getGlobalType());
    doTypeTest();
  }

  public void testStubsConsistencyVariable() {
    myFixture.addFileToProject("a.php", """
      <?php

      /**
       * @psalm-var int $vvv
       */
      $vvv ='a';""");
    Variable variable = PhpIndex.getInstance(getProject()).getVariablesByName("vvv").iterator().next();
    assertEquals(new PhpType().add(PhpType.INT), variable.getGlobalType());
    doTypeTest();
  }

  public void testPsalmVarOnCorrectVariable() {
    doTypeTest(true);
  }

  public void testPsalmArrayShape() {
    doTypeTest(true);
  }

  public void testPsalmMultilineArrayShape() {
    doTypeTest(true);
  }

  public void testPsalmArrayShapeMultipleFiles() {
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

    doTypeTest(true);
  }

  public void testTemplateClassString() {
    doTypeTest();
  }

  public void testTemplateExtends() {
    doTypeTest();
  }

  public void testTemplateExtendsNamespace() {
    doTypeTest();
  }

  public void testTemplateExtendsMultipleTemplates() {
    doTypeTest();
  }

  public void testTemplateImplements() {
    doTypeTest();
  }

  public void testReturnTemplatedClass() {
    doTypeTest();
  }

  public void testTemplatedClassVariableDocType() {
    doTypeTest();
  }

  public void testReturnTemplatedClassNamespace() {
    doTypeTest();
  }

  public void testLocalClassString() {
    doTypeTest();
  }

  public void testLocalClassStringNamespace() {
    doLanguageLevelTest(getProject(), PhpLanguageLevel.PHP800, () -> doTypeTest());
  }

  public void testLocalTypeUnwrappingSameFile() {
    doTypeTest();
  }

  public void testLocalTypeUnwrappingDifferentFile() {
    addPhpFileToProject("a.php", """
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
    doTypeTest();
  }

  public void testTemplatedIteratorInForeach() {
    doTypeTest();
  }

  public void testExtendedClassesWithFallthroughTemplates() {
    doTypeTest();
  }

  public void testExtendedClassesWithFallthroughTemplatesNamespaced() {
    doTypeTest();
  }

  public void testGenericsConstructorInference() {
    doTypeTest();
  }

  public void testMultipleTags() {
    doTypeTest();
  }

  public void testSuperMember() {
    doTypeTest();
  }

  public void testSuperMemberRecursive() {
    doTypeTest();
  }

  public void testSubstituteExtendedClasses() {
    doTypeTest();
  }

  public void testTemplateExtendsByPassing() {
    doTypeTest();
  }

  public void testTemplateExtendsByPassingStaticReturn() {
    doTypeTest();
  }

  public void testTemplateWrappedArray() {
    doTypeTest();
  }
  public void testTemplateUnwrappedArray() {
    doTypeTest();
  }

  public void testTemplateUnwrappedArrayConstructorCall() {
    doTypeTest();
  }

  public void testTemplateUnwrappedArrayConstructorCallNonArrayPassed() {
    doTypeTest();
  }

  public void testTemplateWrappedClass() {
    doTypeTest();
  }

  public void testParameterUnwrap() {
    doTypeTest();
  }

  public void testParameterUnwrapToArray() {
    doTypeTest();
  }

  public void testParameterUnwrapToStaticGeneric() {
    doTypeTest();
  }

  public void testClassNamesFromClassStringDocVar() {
    doTypeTest();
  }

  public void testArrayShapePlural() {
    doTypeTest();
  }

  public void testArrayShapeNumeric() {
    doTypeTest();
  }

  public void testGenericDocMethod() {
    doTypeTest();
  }

  public void testArrayDocMethod() {
    doTypeTest();
  }

  public void testTypeAlias() {
    doTypeTest();
  }

  public void testTypeAliasGlobal() {
    myFixture.addFileToProject("aa.php", """
      <?php

      class Foo {}
      class Bar {}
      /**
       * @psalm-type FooAlias = Foo
       * @psalm-type BarAlias = Bar
       */
      class Phone {

      }""");
    doTypeTest();
  }

  public void testTypeAliasPlural() {
    doTypeTest();
  }

  public void testInferredVarParameter() {
    doTypeTest();
  }

  public void testNestedListInMultilineDoc() {
    doTypeTest();
  }

  public void testGenericYield() {
    doTypeTest();
  }

  public void testGenericYieldDifferentFile() {
    addPhpFileToProject("a.php", """
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
    doTypeTest();
  }

  public void testGenericIteratorBases() {
    doTypeTest();
  }

  public void testGenericIteratorSubstitutionByImplementedMethod() {
    doTypeTest();
  }

  public void testGenericPassingParameter() {
    doTypeTest();
  }

  public void testGenericPassingParameterDifferentFile() {
    addPhpFileToProject("a.php", """
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
    doTypeTest();
  }

  public void testGenericPassingParameterStaticDifferentFile() {
    addPhpFileToProject("a.php", """
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
    doTypeTest();
  }

  public void testGenericPassingParameterStatic() {
    doTypeTest();
  }

  public void testGenericIterableUnwrap() {
    doTypeTest();
  }

  public void testGenericIterableUnwrapDifferentFile() {
    addPhpFileToProject("a.php", """
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
    doTypeTest();
  }

  public void testElementTypeOfGenericClassDifferentFile() {
    addPhpFileToProject("a.php", """
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
    doTypeTest();
  }

  public void testElementTypeOfGenericClass() {
    doTypeTest();
  }

  public void testGenericFlipClassParameters() {
    doTypeTest();
  }

  public void testGenericPassingParameterMultipleSubstitutions() {
    doTypeTest();
  }

  public void testInnerParametrizedParts() {
    // no exception
    doTypeTest();
  }

  public void testAdvancedCallableInferred() {
    doTypeTest();
  }

  public void testAdvancedCallableDoc() {
    doTypeTest();
  }

  public void testAdvancedCallableMethodRef() {
    addPhpFileToProject("a.php", """
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
    doTypeTest();
  }

  public void testTemplatedArrayKey() {
    doTypeTest();
  }

  public void testDocTypesInParentheses() {
    doTypeTest();
  }

  public void testPsalmScalarTypeArrayKey() {
    doTypeTest();
  }

  public void testPsalmNotNumericArrayKey() {
    doTypeTest();
  }

  public void testArrayShapeMultiAssignmentOmittedElements() {
    doTypeTest();
  }

  public void testVarOnPromotedProperty() {
    doTypeTest();
  }

  public void testVarOnPromotedPropertyWithDocCommentOnMethod() {
    doTypeTest();
  }

  public void testParametrisedPolymorphicCallName() {
    addPhpFileToProject("a.php", """
      <?php

      class Two {
          public static function getOne(): One {}
      }
      """);
    doTypeTest();
  }

  public void testKeyOfValueOf() {
    doTypeTest();
  }

  public void testFetchExtendsThroughIntermediateValue() {
    doTypeTest();
  }

  public void testFetchExtendsFromParentClass() {
    doTypeTest();
  }

  public void testExpandFallthroughTemplatesInConstructor() {
    doTypeTest();
  }

  public void testLiteralString() {
    doTypeTest();
  }

  public void testMetaFromSuperParameter() {
    doTypeTest();
  }

  public void testMetaFromSuperParameterGlobal() {
    addPhpFileToProject("a.php", """
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
    doTypeTest();
  }

  public void testMultipleInheritanceWithExtends() {
    doTypeTest();
  }

  public void testLiteralType() {
    doTypeTest();
  }

  public void testNegativeInt() {
    doTypeTest();
  }

  public void testNonNegativeInt() {
    doTypeTest();
  }

  public void testNonPositiveInt() {
    doTypeTest();
  }

  public void testGenericClassStringParameterWithSuperTypeForT() {
    doTypeTest();
  }

  public void testGenericClassStringConstructor() {
    doTypeTest();
  }

  public void testGenericClassStringConstructorWithPromotedProperty() {
    doTypeTest();
  }

  public void testGenericClassStringConstructorWithPromotedPropertyWithParamTag() {
    doTypeTest();
  }

  public void testDocMethodReturnsGenericList() {
    doTypeTest(true);
  }

  public void testGenericAndStatic$static() {
    doTypeTest(true);
  }

  public void testGenericAndStatic$staticWithEntities() {
    doTypeTest(true);
  }

  public void testGenericAndStatic$this() {
    doTypeTest(true);
  }

  public void testGenericAndStatic$arrayObject() {
    doTypeTest(true);
  }

  public void testGenericAndStatic$queryBuilder() {
    doTypeTest(true);
  }

  public void testReturnStaticOrThis$this() {
    doTypeTest(true);
  }

  public void testReturnStaticOrThis$thisWithForeach() {
    doTypeTest(true);
  }

  public void testReturnStaticOrThis$arrayOfThis() {
    doTypeTest(true);
  }

  public void testReturnStaticOrThis$static() {
    doTypeTest(true);
  }

  public void testLocalTypeUnwrappingTagOrderMustNotMatter() {
    doTypeTest(true);
  }

  public void testNewExpressionWithClassString() {
    doTypeTest(true);
  }

  public void testClassStringConstructorParameterTypeStaticCall() {
    doTypeTest(true);
  }
}
