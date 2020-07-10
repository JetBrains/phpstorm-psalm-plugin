package com.jetbrains.php.psalm.lang.documentation.parser;

import com.jetbrains.php.lang.documentation.phpdoc.parser.tags.PhpDocParamTagParser;
import com.jetbrains.php.lang.documentation.phpdoc.parser.tags.PhpDocVarTagParser;
import org.jetbrains.annotations.NotNull;

public class PsalmParamDocParser extends PhpDocParamTagParser{
  @Override
  protected @NotNull PhpDocVarTagParser getBaseParser() {
    return new PsalmDocVarTagParser();
  }
}
