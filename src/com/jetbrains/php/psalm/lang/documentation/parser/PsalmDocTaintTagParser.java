package com.jetbrains.php.psalm.lang.documentation.parser;

import com.jetbrains.php.lang.documentation.phpdoc.parser.tags.PhpDocTagParser;
import com.jetbrains.php.lang.documentation.phpdoc.parser.tags.PhpDocVarTagParser;
import com.jetbrains.php.lang.parser.PhpPsiBuilder;

public class PsalmDocTaintTagParser extends PhpDocTagParser {
  @Override
  protected boolean parseContents(PhpPsiBuilder builder) {
    if (!parseTypes(builder)) return false;
    builder.compareAndEat(DOC_RPAREN);
    PhpDocVarTagParser.parseVar(builder);
    builder.compareAndEat(DOC_ARROW);
    builder.compareAndEat(DOC_IDENTIFIER);
    return true;
  }
}
