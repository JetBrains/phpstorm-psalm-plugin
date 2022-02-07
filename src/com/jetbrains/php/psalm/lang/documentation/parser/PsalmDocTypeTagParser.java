package com.jetbrains.php.psalm.lang.documentation.parser;

import com.jetbrains.php.lang.documentation.phpdoc.parser.tags.PhpDocTagParser;
import com.jetbrains.php.lang.parser.PhpPsiBuilder;

public class PsalmDocTypeTagParser extends PhpDocTagParser {
  @Override
  protected boolean parseContents(PhpPsiBuilder builder) {
    if (builder.compareAndEat(DOC_IDENTIFIER) && PsalmDocVarTagParser.compareAndEatText(builder, "=")) {
      parseTypes(builder);
    }
    return super.parseContents(builder);
  }
}
