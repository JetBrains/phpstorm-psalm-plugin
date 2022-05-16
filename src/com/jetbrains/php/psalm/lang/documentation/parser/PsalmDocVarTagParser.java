package com.jetbrains.php.psalm.lang.documentation.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.jetbrains.php.lang.documentation.phpdoc.parser.tags.PhpDocVarTagParser;
import com.jetbrains.php.lang.parser.ParserPart;
import com.jetbrains.php.lang.parser.PhpElementTypes;
import com.jetbrains.php.lang.parser.PhpPsiBuilder;
import org.jetbrains.annotations.NotNull;

import static com.jetbrains.php.lang.parser.ListParsingHelper.parseDelimitedExpressionWithLeadExpr;

public class PsalmDocVarTagParser extends PhpDocVarTagParser { }
