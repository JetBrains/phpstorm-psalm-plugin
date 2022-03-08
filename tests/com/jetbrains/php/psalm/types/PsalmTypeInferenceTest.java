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

  public void testNonEmptyString() {
    doTypeTest();
  }

  public void testIterableKey() {
    doTypeTest();
  }

  public void testAdvancedCallable() {
    myFixture.addFileToProject("a.php", "<?php\n" +
                                        "class Foo {}\n" +
                                        "/**\n" +
                                        " * @return Closure(bool, int|string, $a int) : int|Foo\n" +
                                        " */\n" +
                                        "function a(): Closure{\n" +
                                        "\n" +
                                        "}\n" +
                                        "\n" +
                                        "function b(){\n" +
                                        "    return a();\n" +
                                        "}");
    doTypeTest();
  }

  public void testAdvancedCallableInSameFile() {
    doTypeTest();
  }

  public void testAdvancedCallableImported() {
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
    myFixture.addFileToProject("a.php", "<?php\n" +
                                        "\n" +
                                        "/**\n" +
                                        " * @psalm-return int\n" +
                                        " */\n" +
                                        "function f(): string\n" +
                                        "{\n" +
                                        "}");
    Function function = PhpIndex.getInstance(getProject()).getFunctionsByFQN("\\f").iterator().next();
    assertEquals(new PhpType().add(PhpType.INT).add(PhpType.STRING), function.getGlobalType());
    doTypeTest();
  }

  public void testStubsConsistencyParameter() {
    myFixture.addFileToProject("a.php", "<?php\n" +
                                        "\n" +
                                        "/**\n" +
                                        " * @psalm-param int $a\n" +
                                        " */\n" +
                                        "function f(string $a)\n" +
                                        "{\n" +
                                        "}");
    Parameter param = PhpIndex.getInstance(getProject()).getFunctionsByFQN("\\f").iterator().next().getParameter(0);
    assertEquals(new PhpType().add(PhpType.INT).add(PhpType.STRING), param.getGlobalType());
    doTypeTest();
  }

  public void testStubsConsistencyField() {
    myFixture.addFileToProject("a.php", "<?php\n" +
                                        "\n" +
                                        "class A {\n" +
                                        "    /**\n" +
                                        "     * @psalm-var int $f\n" +
                                        "     */\n" +
                                        "    public string $f;\n" +
                                        "}");
    Field field = PhpIndex.getInstance(getProject()).getClassesByFQN("\\A").iterator().next().getOwnFields()[0];
    assertEquals(new PhpType().add(PhpType.INT).add(PhpType.STRING), field.getGlobalType());
    doTypeTest();
  }

  public void testStubsConsistencyVariable() {
    myFixture.addFileToProject("a.php", "<?php\n" +
                                        "\n" +
                                        "/**\n" +
                                        " * @psalm-var int $vvv\n" +
                                        " */\n" +
                                        "$vvv ='a';");
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
    myFixture.addFileToProject("aa.php", "<?php\n" +
                                        "/**\n" +
                                        " * @psalm-return array{age?: Exception, f: int}\n" +
                                        " */\n" +
                                        "function f(){}");
    myFixture.addFileToProject("b.php", "<?php\n" +
                                        "\n" +
                                        "function ff() {\n" +
                                        "    return f();\n" +
                                        "}");
    myFixture.addFileToProject("c.php", "<?php\n" +
                                         "/**\n" +
                                         " * @psalm-return array<array{age?: Exception, f: int}>\n" +
                                         " */\n" +
                                         "function ff1(){}");
    myFixture.addFileToProject("d.php", "<?php\n" +
                                        "class AA {\n" +
                                        "/**\n" +
                                        " * @psalm-return array{age?: Exception, f: int}\n" +
                                        " */\n" +
                                        "function ff1(){}\n" +
                                        "}");

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
    addPhpFileToProject("a.php", "<?php" +
                                 "\n" +
                                 "class C\n" +
                                 "{\n" +
                                 "}\n" +
                                 "\n" +
                                 "/**\n" +
                                 " * @template T\n" +
                                 " */\n" +
                                 "class Bar\n" +
                                 "{\n" +
                                 "    /**\n" +
                                 "     * @psalm-return T\n" +
                                 "     */\n" +
                                 "    public function doBaz()\n" +
                                 "    {\n" +
                                 "    }\n" +
                                 "}\n");
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

  public void testTemplateWrappedClass() {
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
    myFixture.addFileToProject("aa.php", "<?php\n" +
                                         "\n" +
                                         "class Foo {}\n" +
                                         "class Bar {}\n" +
                                         "/**\n" +
                                         " * @psalm-type FooAlias = Foo\n" +
                                         " * @psalm-type BarAlias = Bar\n" +
                                         " */\n" +
                                         "class Phone {\n" +
                                         "\n" +
                                         "}");
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
}
