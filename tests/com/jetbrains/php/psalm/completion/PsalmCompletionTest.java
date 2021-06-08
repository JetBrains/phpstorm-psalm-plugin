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
      , "trait-string"
      , "non-empty-list"
      , "non-empty-array"
    );
  }

  public void testArrayShape() {
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
}
