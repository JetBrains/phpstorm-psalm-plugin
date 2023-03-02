package com.jetbrains.php.psalm.lang.documentation.parser;

import com.jetbrains.php.lang.documentation.phpdoc.parser.tags.PhpDocTagParser;
import com.jetbrains.php.lang.parser.PhpPsiBuilder;

public class PsalmDocAssertTagParser extends PhpDocTagParser {
  @Override
  public boolean parseContents(PhpPsiBuilder builder) {
    return parseAssert(builder);
  }
}
