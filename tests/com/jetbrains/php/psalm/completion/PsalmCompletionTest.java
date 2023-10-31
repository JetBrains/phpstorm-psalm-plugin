package com.jetbrains.php.psalm.completion;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.testFramework.NeedsIndex;
import com.jetbrains.php.PhpIcons;
import com.jetbrains.php.fixtures.PhpCompletionTestCase;
import com.jetbrains.php.psalm.types.PsalmTypeInferenceTest;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class PsalmCompletionTest extends PhpCompletionTestCase {

  @Override
  protected @NotNull String getTestDataHome() {
    return PsalmTypeInferenceTest.TEST_DATA_HOME;
  }

  @Override
  protected String getFixtureTestDataFolder() {
    return "completion";
  }

  public void testCustomDeclaredTypes() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings()
      , "MyTemp1"
      , "MyTemp2"
      , "MyTemp3"
    );

  }

  public void testCustomDocTypes() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "class-string"
      , "callable-string"
      , "numeric-string"
      , "lowercase-string"
      , "non-empty-lowercase-string"
      , "non-falsy-string"
      , "truthy-string"
      , "scalar"
      , "numeric"
      , "array-key"
      , "empty"
      , "key-of"
      , "value-of"
      , "trait-string"
      , "non-empty-list"
      , "non-empty-array"
    );
  }

  public void testArrayShape() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "name", "age");
  }

  public void testNestedArrayShape() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "name", "age");
  }

  public void testNestedListArrayShape() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "name", "age");
  }

  public void testNestedArrayShapeDifferentQuotes() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "name", "age");
  }

  public void testNestedMultilineArrayShape() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "name", "age");
  }

  @NeedsIndex.Full
  public void testArrayShapeMultipleFiles() {
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
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "name", "age");
  }

  @NeedsIndex.Full
  public void testNoMaterializationForTemplateExtends() {
    addPhpFileToProject("a.php", """
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
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "f");
  }

  @NeedsIndex.Full
  public void testStaticMemberReferenceDynamicClassFQN() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "sayHello", "$sProperty");
  }

  public void testArrayShapeQuotedKeys() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "a", "b","c","d","f","g");
  }

  public void testArrayShapeQuotedKeysMultiple() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "first", "second");
  }

  public void testPsalmTraceTagCompletion() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "psalm-trace");
  }

  public void testPsalmAliasImport() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "MyFooAlias", "MyBarAlias");
  }

  public void testArrayShapePropertyTag() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "name");
  }

  public void testArrayShapePropertyTagAndVarTag() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "name", "surname");
  }

  public void testArrayShapePropertyTagAndVarTagClassDocComment() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "name");
  }

  public void testArrayShapeUnion() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "prop1", "prop2", "prop3", "prop4");
  }

  public void testNestedListArrayShapeUnion() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "prop1", "prop2");
  }

  public void testNestedArrayShapeUnion() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "name", "surname");
  }

  public void testArrayShapeFromConstructorCompletion() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "first", "last");
  }

  @NeedsIndex.Full
  public void testExpectedArgumentCompletionFromKeyOf() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "a", "b", "c");
  }

  public void testNestedArrayShapeForeach() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "name", "age");
  }

  public void testNestedArrayShapeForeachOverArrayAccess() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "name", "age");
  }

  public void testConditionalType() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "int", "string");
  }

  public void testObjectShapeDocParam() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "foo");
  }

  public void testObjectShapeDocProperty() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "foo");
  }

  public void testObjectShapeField() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "foo");
  }

  public void testObjectShapeFunction() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "baz");
  }

  public void testObjectShapeVariable() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "name", "age");
  }

  @NeedsIndex.Full
  public void testObjectShapeMultipleFiles() {
    addPhpFileToProject("a.php", """
      <?php
      
      /** @psalm-return object{ foo: Foo, bar: object{ baz: string }} */
      function f() {
      
      }""");
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "foo", "bar");
  }

  public void testObjectShapePhysicalField() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "name");
  }

  public void testObjectShapeConstructor() {
    addPhpFileToProject("a.php", """
      <?php
      
      class Foo {
          /**
           * @return object{name: string, age: int}
           */
          function __construct() {}
      }""");
    doInitCompletion();
    assertDoesntContain(myFixture.getLookupElementStrings(), "name");
  }

  public void testArrayAndObjectShapes() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "abc");
  }

  @NeedsIndex.Full
  public void testArrayAndObjectShapesMultipleFiles() {
    addPhpFileToProject("a.php", """   
      <?php
      /**
       * @psalm-return array{data: array{key: object{abc : int}}}
       */
      function foo() {}
      """);
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "abc");
  }

  public void testArrayShapeArrayAccessIntIndex() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "key");
  }

  public void testObjectAndArrayShapes() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "abc");
  }

  @NeedsIndex.Full
  public void testObjectAndArrayShapesMultipleFiles() {
    addPhpFileToProject("a.php", """   
      <?php
      /**
       * @psalm-return object{data: object{key: array{abc : int}}
       */
      function foo() {}
      """);
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "abc");
  }

  public void testObjectAndArrayShapesMixed() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "amb");
  }

  @NeedsIndex.Full
  public void testGenericMixins$simple() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "genericFooMethod", "mixinClassProperty", "mixinClassMethod");
  }

  @NeedsIndex.Full
  public void testGenericMixins$decorator() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "request");
  }

  @NeedsIndex.Full public void testGenericMixins$stdlibClassMixin() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "name", "getConstant", "setStaticPropertyValue");
  }

  @NeedsIndex.Full
  public void testGenericMixins$severalMixins() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "mixinClassProperty1", "mixinClassMethod1", "mixinClassProperty2", "mixinClassMethod2");
  }

  @NeedsIndex.Full
  public void testGenericMixins$genericAndPlainMixins() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "mixinClassProperty1", "mixinClassMethod1", "mixinClassProperty2", "mixinClassMethod2");
  }

  @NeedsIndex.Full
   public void testGenericMixins$unionTwoClassesWithMixins() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "mixinClassProperty1", "mixinClassMethod1", "mixinClassProperty2", "mixinClassMethod2");
  }

  @NeedsIndex.Full
  public void testGenericMixins$genericMixinsInOtherFile() {
    myFixture.addFileToProject("aa.php", """
      <?php
      
      class MixinClass
      {
        public string $mixinClassProperty;
      
        public function mixinClassMethod() {}
      }
      """);
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "genericFooMethod", "mixinClassProperty", "mixinClassMethod");
  }

  public void testTemplateParameters() {
    doInitCompletion();

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

    LookupElement[] elements = myFixture.getLookupElements();
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
      assertNotNull("Unhandled '" + name + "' completion item", expectedString);
      assertEquals("Wrong completion item for '" + name + "'", expectedString, actualString);
    }
  }
}
