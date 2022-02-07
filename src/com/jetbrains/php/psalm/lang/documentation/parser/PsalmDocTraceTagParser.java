package com.jetbrains.php.psalm.lang.documentation.parser;

import com.jetbrains.php.lang.documentation.phpdoc.parser.tags.PhpDocVarTagParser;
import com.jetbrains.php.lang.parser.PhpPsiBuilder;

public class PsalmDocTraceTagParser extends PhpDocVarTagParser {

  @Override
  protected boolean parseVariableTypes(PhpPsiBuilder builder) {
    return true;
  }
}
