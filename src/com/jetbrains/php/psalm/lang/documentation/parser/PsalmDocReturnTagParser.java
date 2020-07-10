package com.jetbrains.php.psalm.lang.documentation.parser;

import com.jetbrains.php.lang.documentation.phpdoc.parser.tags.PhpDocReturnTagParser;
import com.jetbrains.php.lang.parser.PhpPsiBuilder;

public class PsalmDocReturnTagParser extends PhpDocReturnTagParser {
  @Override
  public boolean parseContents(PhpPsiBuilder builder) {
    return PsalmDocVarTagParser.tryParseExtendedConstantReferences(builder) || super.parseContents(builder);
  }
}
