package com.jetbrains.php.psalm.completion;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.openapi.project.Project;
import com.intellij.testFramework.NeedsIndex;
import com.intellij.testFramework.TestIndexingModeSupporter.IndexingMode;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;
import com.intellij.testFramework.junit5.TestApplication;
import com.intellij.testFramework.junit5.fixture.TestFixture;
import com.jetbrains.php.PhpIcons;
import com.jetbrains.php.fixtures.PhpCompletionTestFixture;
import com.jetbrains.php.fixtures.PhpIndexingModeTest;
import com.jetbrains.php.fixtures.PhpTestDataLayout;
import com.jetbrains.php.psalm.types.PsalmTypeInferenceTest;

import java.util.HashMap;

import static com.jetbrains.php.fixtures.PhpCompletionTestFixture.phpCompletionFixture;
import static com.jetbrains.php.fixtures.PhpTestFixtures.phpProjectFixture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestApplication
public class PsalmCompletionTest {
  private static final TestFixture<Project> projectFixture = phpProjectFixture();
  private final TestFixture<PhpCompletionTestFixture> php =
    phpCompletionFixture(projectFixture, new PhpTestDataLayout("completion").withHome(PsalmTypeInferenceTest.TEST_DATA_HOME));

  @PhpIndexingModeTest
  public void testCustomDeclaredTypes(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("MyTemp1", "MyTemp2", "MyTemp3");
    });
  }

  @PhpIndexingModeTest
  public void testCustomDocTypes(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("callable-string", "numeric-string", "lowercase-string", "non-empty-lowercase-string", "non-falsy-string", "truthy-string", "scalar", "numeric", "array-key", "empty", "key-of", "value-of", "trait-string", "non-empty-list", "non-empty-array");
    });
  }

  @PhpIndexingModeTest
  public void testArrayShape(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("name", "age");
    });
  }

  @PhpIndexingModeTest
  public void testNestedArrayShape(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("name", "age");
    });
  }

  @PhpIndexingModeTest
  public void testNestedListArrayShape(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("name", "age");
    });
  }

  @PhpIndexingModeTest
  public void testNestedArrayShapeDifferentQuotes(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("name", "age");
    });
  }

  @PhpIndexingModeTest
  public void testNestedMultilineArrayShape(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("name", "age");
    });
  }

  @NeedsIndex.Full
  @PhpIndexingModeTest
  public void testArrayShapeMultipleFiles(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      CodeInsightTestFixture myFixture = php.get().getFixture();
      myFixture.addFileToProject("aa.php", """
        <?php
        /**
         * @psalm-return array{age?: Exception, "name": string}
         */
        function f(){}""");
      myFixture.addFileToProject("b.php", """
        <?php

        function ff() {
            return f();
        }""");
      php.get().doInitCompletion();
      assertThat(myFixture.getLookupElementStrings()).contains("name", "age");
    });
  }

  @NeedsIndex.Full
  @PhpIndexingModeTest
  public void testNoMaterializationForTemplateExtends(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().addPhpFileToProject("a.php", """
        <?php

        /**
         * @template T
         */
        abstract class Base
        {
            /**
             * @return T
             */
            public function item() {}
        }

        /**
         * @extends Base<P1>
         */
        class C extends Base {}\s""");
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("f");
    });
  }

  @NeedsIndex.Full
  @PhpIndexingModeTest
  public void testStaticMemberReferenceDynamicClassFQN(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("sayHello", "$sProperty");
    });
  }

  @PhpIndexingModeTest
  public void testArrayShapeQuotedKeys(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("a", "b", "c", "d", "f", "g");
    });
  }

  @PhpIndexingModeTest
  public void testArrayShapeQuotedKeysMultiple(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("first", "second");
    });
  }

  @PhpIndexingModeTest
  public void testPsalmTraceTagCompletion(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("psalm-trace");
    });
  }

  @PhpIndexingModeTest
  public void testPsalmAliasImport(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("MyFooAlias", "MyBarAlias");
    });
  }

  @PhpIndexingModeTest
  public void testArrayShapePropertyTag(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("name");
    });
  }

  @PhpIndexingModeTest
  public void testArrayShapePropertyTagAndVarTag(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("name", "surname");
    });
  }

  @PhpIndexingModeTest
  public void testArrayShapePropertyTagAndVarTagClassDocComment(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("name");
    });
  }

  @PhpIndexingModeTest
  public void testArrayShapeUnion(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("prop1", "prop2", "prop3", "prop4");
    });
  }

  @PhpIndexingModeTest
  public void testNestedListArrayShapeUnion(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("prop1", "prop2");
    });
  }

  @PhpIndexingModeTest
  public void testNestedArrayShapeUnion(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("name", "surname");
    });
  }

  @PhpIndexingModeTest
  public void testArrayShapeFromConstructorCompletion(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("first", "last");
    });
  }

  @NeedsIndex.Full
  @PhpIndexingModeTest
  public void testExpectedArgumentCompletionFromKeyOf(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("a", "b", "c");
    });
  }

  @PhpIndexingModeTest
  public void testNestedArrayShapeForeach(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("name", "age");
    });
  }

  @PhpIndexingModeTest
  public void testNestedArrayShapeForeachOverArrayAccess(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("name", "age");
    });
  }

  @PhpIndexingModeTest
  public void testArrayShapeNestedForeach(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("somekey");
    });
  }

  @PhpIndexingModeTest
  public void testConditionalType(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("int", "string");
    });
  }

  @PhpIndexingModeTest
  public void testObjectShapeDocParam(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("foo");
    });
  }

  @PhpIndexingModeTest
  public void testObjectShapeDocProperty(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("foo");
    });
  }

  @PhpIndexingModeTest
  public void testObjectShapeField(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("foo");
    });
  }

  @PhpIndexingModeTest
  public void testObjectShapeFunction(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("baz");
    });
  }

  @PhpIndexingModeTest
  public void testObjectShapeVariable(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("name", "age");
    });
  }

  @NeedsIndex.Full
  @PhpIndexingModeTest
  public void testObjectShapeMultipleFiles(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().addPhpFileToProject("a.php", """
        <?php
      
        /** @psalm-return object{ foo: Foo, bar: object{ baz: string }} */
        function f() {
      
        }""");
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("foo", "bar");
    });
  }

  @PhpIndexingModeTest
  public void testObjectShapePhysicalField(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("name");
    });
  }

  @PhpIndexingModeTest
  public void testObjectShapeConstructor(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().addPhpFileToProject("a.php", """
        <?php
      
        class Foo {
            /**
             * @return object{name: string, age: int}
             */
            function __construct() {}
        }""");
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).doesNotContain("name");
    });
  }

  @PhpIndexingModeTest
  public void testArrayAndObjectShapes(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("abc");
    });
  }

  @NeedsIndex.Full
  @PhpIndexingModeTest
  public void testArrayAndObjectShapesMultipleFiles(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().addPhpFileToProject("a.php", """   
        <?php
        /**
         * @psalm-return array{data: array{key: object{abc : int}}}
         */
        function foo() {}
        """);
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("abc");
    });
  }

  @PhpIndexingModeTest
  public void testArrayShapeArrayAccessIntIndex(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("key");
    });
  }

  @PhpIndexingModeTest
  public void testObjectAndArrayShapes(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("abc");
    });
  }

  @PhpIndexingModeTest
  public void testObjectShapeArrayAccessIntIndex(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("key");
    });
  }

  @PhpIndexingModeTest
  public void testObjectShapeIndexedArray(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("key");
    });
  }

  @PhpIndexingModeTest
  public void testNestedObjectShapeArrayItemTicketSample(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("ProductIdentifier", "ProductTitle", "Updated");
    });
  }

  @PhpIndexingModeTest
  public void testNestedObjectShapeArrayItemDirect(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("name", "id");
    });
  }

  @PhpIndexingModeTest
  public void testNestedObjectShapeArrayItemAssigned(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("name", "id");
    });
  }

  @PhpIndexingModeTest
  public void testNestedObjectShapeArrayItemForeach(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("name", "id");
    });
  }

  @PhpIndexingModeTest
  public void testNestedObjectShapeArrayItemNestedProperty(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("name", "id");
    });
  }

  @PhpIndexingModeTest
  public void testNestedObjectShapeMultidimensionalArrayItemDirect(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("name", "id");
    });
  }

  @PhpIndexingModeTest
  public void testNestedObjectShapeUnionArrayItemAssigned(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("name", "id");
    });
  }

  @PhpIndexingModeTest
  public void testNestedObjectShapeTupleItemCompletionIsIndexSpecific(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      CodeInsightTestFixture myFixture = php.get().getFixture();
      php.get().doInitCompletion();
      assertThat(myFixture.getLookupElementStrings()).contains("name");
      assertThat(myFixture.getLookupElementStrings()).doesNotContain("id");
    });
  }

  @NeedsIndex.Full
  @PhpIndexingModeTest
  public void testObjectAndArrayShapesMultipleFiles(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().addPhpFileToProject("a.php", """   
        <?php
        /**
         * @psalm-return object{data: object{key: array{abc : int}}
         */
        function foo() {}
        """);
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("abc");
    });
  }

  @PhpIndexingModeTest
  public void testObjectAndArrayShapesMixed(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("amb");
    });
  }

  @NeedsIndex.Full
  @PhpIndexingModeTest
  public void testGenericMixins$simple(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("genericFooMethod", "mixinClassProperty", "mixinClassMethod");
    });
  }

  @NeedsIndex.Full
  @PhpIndexingModeTest
  public void testGenericMixins$decorator(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("request");
    });
  }

  @NeedsIndex.Full
  @PhpIndexingModeTest
  public void testGenericMixins$stdlibClassMixin(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("name", "getConstant", "setStaticPropertyValue");
    });
  }

  @NeedsIndex.Full
  @PhpIndexingModeTest
  public void testGenericMixins$severalMixins(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("mixinClassProperty1", "mixinClassMethod1", "mixinClassProperty2", "mixinClassMethod2");
    });
  }

  @NeedsIndex.Full
  @PhpIndexingModeTest
  public void testGenericMixins$genericAndPlainMixins(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("mixinClassProperty1", "mixinClassMethod1", "mixinClassProperty2", "mixinClassMethod2");
    });
  }

  @NeedsIndex.Full
  @PhpIndexingModeTest
  public void testGenericMixins$unionTwoClassesWithMixins(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).contains("mixinClassProperty1", "mixinClassMethod1", "mixinClassProperty2", "mixinClassMethod2");
    });
  }

  @PhpIndexingModeTest
  public void testDocTagCompletions(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doTestLookupElementsContains("psalm-require-extends", "psalm-require-implements");
    });
  }

  @NeedsIndex.Full
  @PhpIndexingModeTest
  public void testGenericMixins$genericMixinsInOtherFile(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      CodeInsightTestFixture myFixture = php.get().getFixture();
      myFixture.addFileToProject("aa.php", """
        <?php
      
        class MixinClass
        {
          public string $mixinClassProperty;
      
          public function mixinClassMethod() {}
        }
        """);
      php.get().doInitCompletion();
      assertThat(myFixture.getLookupElementStrings()).contains("genericFooMethod", "mixinClassProperty", "mixinClassMethod");
    });
  }

  @PhpIndexingModeTest
  public void testTemplateParameters(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();

      HashMap<String, String> testCases = new HashMap<>();
      testCases.put("TPClassSimple",
                    "TPClassSimple template parameter of \\Foo");
      testCases.put("TPClassWithSuper",
                    "TPClassWithSuper template parameter of \\Foo extends Base");
      testCases.put("TPCovariantClass",
                    "TPCovariantClass covariant template parameter of \\Foo");
      testCases.put("TPCovariantClassWithSuper",
                    "TPCovariantClassWithSuper covariant template parameter of \\Foo extends Base");
      testCases.put("TPContravariantClassWithSuper",
                    "TPContravariantClassWithSuper contravariant template parameter of \\Foo extends Base");

      testCases.put("TPFunctionSimple",
                    "TPFunctionSimple template parameter of method()");
      testCases.put("TPFunctionWithSuper",
                    "TPFunctionWithSuper template parameter of method() extends Base");
      testCases.put("TPCovariantFunction",
                    "TPCovariantFunction covariant template parameter of method()");
      testCases.put("TPCovariantFunctionWithSuper",
                    "TPCovariantFunctionWithSuper covariant template parameter of method() extends Base");
      testCases.put("TPContravariantFunctionWithSuper",
                    "TPContravariantFunctionWithSuper contravariant template parameter of method() extends Base");

      LookupElement[] elements = php.get().getFixture().getLookupElements();
      for (LookupElement element : elements) {
        String name = element.getLookupString();
        LookupElementPresentation presentation = new LookupElementPresentation();
        element.renderElement(presentation);
        if (presentation.getIcon() != PhpIcons.TEMPLATE_PARAMETER) {
          continue;
        }

        String actualString = presentation.getItemText() + presentation.getTailText();
        String typeText = presentation.getTypeText();
        if (typeText != null && !typeText.isEmpty()) {
          actualString += " " + typeText;
        }
        String expectedString = testCases.get(name);
        assertNotNull(expectedString, "Unhandled '" + name + "' completion item");
        assertEquals(expectedString, actualString, "Wrong completion item for '" + name + "'");
      }
    });
  }

  @NeedsIndex.Full
  @PhpIndexingModeTest
  public void testStaticMethodsCompletionViaClassStringForeachValue(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).containsExactlyInAnyOrder("staticMethod", "staticMethod2", "class");
    });
  }

  @NeedsIndex.Full
  @PhpIndexingModeTest
  public void testStaticMethodsCompletionViaClassStringForeachKey(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      php.get().doInitCompletion();
      assertThat(php.get().getFixture().getLookupElementStrings()).containsExactlyInAnyOrder("staticMethod", "staticMethod2", "class");
    });
  }

  @NeedsIndex.Full
  @PhpIndexingModeTest
  public void testArrayShapeWithLineComment(IndexingMode mode) {
    php.get().runInMode(mode, () -> {
      CodeInsightTestFixture myFixture = php.get().getFixture();
      php.get().doInitCompletion();
      assertThat(myFixture.getLookupElementStrings()).containsExactlyInAnyOrder("key1", "key2");
      assertThat(myFixture.getLookupElementStrings()).doesNotContain("comment", "Comment");
    });
  }

}
