package com.jetbrains.php.psalm.lang;

import com.intellij.psi.PsiDocCommentBase;
import com.intellij.util.CollectConsumer;
import com.jetbrains.php.fixtures.PhpCodeInsightFixtureTestCase;
import com.jetbrains.php.lang.documentation.PhpDocumentationProvider;
import com.jetbrains.php.psalm.types.PsalmTypeInferenceTest;
import org.jetbrains.annotations.NotNull;

public class PsalmDocumentationProviderTest extends PhpCodeInsightFixtureTestCase {
    @Override
    protected @NotNull String getTestDataHome() {
        return PsalmTypeInferenceTest.TEST_DATA_HOME;
    }

    @Override
    protected String getFixtureTestDataFolder() {
        return "codeInsight/documentationProvider";
    }

    @NotNull
    @Override
    protected String getFileAfterExtension() {
        return "html";
    }

    public void doTestRenderedDoc() {
        configureByFile();
        PhpDocumentationProvider docProvider = new PhpDocumentationProvider();
        CollectConsumer<PsiDocCommentBase> docComments = new CollectConsumer<>();
        PhpDocumentationProvider provider = new PhpDocumentationProvider();
        provider.collectDocComments(myFixture.getFile(), docComments);
        StringBuilder builder = new StringBuilder();
        docComments.getResult().forEach(c -> {
            builder.append(docProvider.generateRenderedDoc(c));
            builder.append("\n");
        });
        String res = builder.toString().replace("<br>", "<br>\n").replace("<p>", "\n<p>").replace("<table>", "\n<table>").replace("<tr>", "\n<tr>");
        configurePhpByText("result.html", res);
        checkResultByFile();
    }

    public void testPsalmTags() {
        doTestRenderedDoc();
    }

    public void testVarTag() {
      doTestRenderedDoc();
    }

    public void testCustomPsalmTags() {
      doTestRenderedDoc();
    }

    public void testTemplateTag() {
      doTestRenderedDoc();
    }
}
