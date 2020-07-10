package com.jetbrains.php.psalm.lang.documentation.parser;

import com.intellij.lang.PsiBuilder;
import com.jetbrains.php.lang.documentation.phpdoc.parser.tags.PhpDocVarTagParser;
import com.jetbrains.php.lang.parser.ParserPart;
import com.jetbrains.php.lang.parser.PhpElementTypes;
import com.jetbrains.php.lang.parser.PhpPsiBuilder;
import com.jetbrains.php.lang.parser.parsing.Namespace;
import org.jetbrains.annotations.NotNull;

import static com.jetbrains.php.lang.parser.ListParsingHelper.parseDelimitedExpressionWithLeadExpr;

public class PsalmDocVarTagParser extends PhpDocVarTagParser {
  private static final ParserPart PART = builder -> {
    PsiBuilder.Marker docType = builder.mark();
    PsiBuilder.Marker ref = builder.mark();
    Namespace.parseReference(builder);
    if (!parseExtendedClassReference(builder)) {
      ref.rollbackTo();
      docType.rollbackTo();
      return PhpElementTypes.EMPTY_INPUT;
    }
    ref.done(phpDocRef);
    docType.done(phpDocType);
    return phpDocType;
  };

  private static boolean parseExtendedClassReference(@NotNull PhpPsiBuilder builder) {
    if (!builder.compareAndEat(DOC_IDENTIFIER) || !builder.compareAndEat(DOC_STATIC) || !builder.compareAndEat(DOC_IDENTIFIER)) {
      return false;
    }
    if (builder.getTokenType() == DOC_TEXT && "*".equals(builder.getTokenText())) {
      builder.advanceLexer();
    }
    return true;
  }

  @Override
  protected boolean parseVariableTypes(PhpPsiBuilder builder) {
    return tryParseExtendedConstantReferences(builder) || super.parseVariableTypes(builder);
  }

  public static boolean tryParseExtendedConstantReferences(@NotNull PhpPsiBuilder builder) {
    PsiBuilder.Marker mark = builder.mark();
    if (parseDelimitedExpressionWithLeadExpr(builder, PART.parse(builder), PART, DOC_PIPE, true, false) == 0) {
      mark.rollbackTo();
      return false;
    }

    mark.drop();
    return true;
  }
}
