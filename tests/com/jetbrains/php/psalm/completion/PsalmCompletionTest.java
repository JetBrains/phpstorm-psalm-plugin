package com.jetbrains.php.psalm.completion;

import com.jetbrains.php.fixtures.PhpCompletionTestCase;
import com.jetbrains.php.psalm.types.PsalmTypeInferenceTest;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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

  public void testArrayShapeMultipleFiles() {
    myFixture.addFileToProject("aa.php", "<?php\n" +
                                         "/**\n" +
                                         " * @psalm-return array{age?: Exception, \"name\": string}\n" +
                                         " */\n" +
                                         "function f(){}");
    myFixture.addFileToProject("b.php", "<?php\n" +
                                        "\n" +
                                        "function ff() {\n" +
                                        "    return f();\n" +
                                        "}");
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "name", "age");
  }

  public void testNoMaterializationForTemplateExtends() {
    addPhpFileToProject("a.php", "<?php\n" +
                                 "\n" +
                                 "/**\n" +
                                 " * @template T\n" +
                                 " */\n" +
                                 "abstract class Base\n" +
                                 "{\n" +
                                 "    /**\n" +
                                 "     * @return T\n" +
                                 "     */\n" +
                                 "    public function item() {}\n" +
                                 "}\n" +
                                 "\n" +
                                 "/**\n" +
                                 " * @extends Base<P1>\n" +
                                 " */\n" +
                                 "class C extends Base {} ");
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "f");
  }

  public void testStaticMemberReferenceDynamicClassFQN() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "sayHello", "$sProperty");
  }

  public void testArrayShapeQuotedKeys() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "a", "b","c","d","f","g");
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

  public void testExpectedArgumentCompletionFromKeyOf() {
    doInitCompletion();
    assertContainsElements(myFixture.getLookupElementStrings(), "a", "b", "c");
  }
}
