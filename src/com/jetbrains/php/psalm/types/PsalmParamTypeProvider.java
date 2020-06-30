package com.jetbrains.php.psalm.types;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.text.StringTokenizer;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

public class PsalmParamTypeProvider implements PhpTypeProvider4 {

  @Override
  public char getKey() {
    return 'ᢒ';
  }

  @Override
  public @Nullable PhpType getType(PsiElement element) {
    return element instanceof PhpDocType ? parsePsalmType(element.getText()) : null;
  }

  public static PhpType parsePsalmType(String type) {
    PsalmType psalmType = parsePsalmType(new PsalmArrayTokenizer(type), 0);
    return psalmType.parsed() ? psalmType.getType() : null;
  }

  private static class PsalmArrayTokenizer extends StringTokenizer {
    private String myCurrentToken = null;

    private PsalmArrayTokenizer(@NotNull String str) {
      super(str, " \t,><", true);
    }

    private  @Nullable String next() {
      if (!hasMoreTokens()) {
        return null;
      }
      return myCurrentToken = getNextTokenSkippingWhitespace();
    }


    @Nullable
    private String getNextTokenSkippingWhitespace() {
      String nextToken = nextToken();
      while (nextToken.trim().isEmpty()) {
        if (!hasMoreTokens()) return null;
        nextToken = nextToken();
      }
      return nextToken;
    }
  }

  private static class PsalmType extends Pair<PhpType, Boolean> {
    private PsalmType(PhpType type, boolean parsedArray) {
      super(type, parsedArray);
    }

    public PhpType getType() {
      return first;
    }

    private boolean parsed() {
      return second;
    }
  }

  public static PsalmType parsePsalmType(PsalmArrayTokenizer tokenizer, int depth) {
    //identifier|array<template [, template]>
    String identifier = StringUtil.notNullize(tokenizer.next());
    String openingTag = tokenizer.next();
    if (!StringUtil.equalsIgnoreCase(openingTag, "<")) {
      PhpType type = new PhpType();
      StringUtil.split(identifier, "|").forEach(type::add);
      return new PsalmType(type.pluralise(depth), false);
    }
    PhpType key = parsePsalmType(tokenizer, depth + 1).getType();
    PhpType value = StringUtil.equalsIgnoreCase(tokenizer.myCurrentToken, ",") ? parsePsalmType(tokenizer, depth + 1).getType() : key;
    tokenizer.next(); // ">"
    return new PsalmType(value, true);
  }

  @Override
  public @Nullable PhpType complete(String expression, Project project) {
    return null;
  }

  @Override
  public Collection<? extends PhpNamedElement> getBySignature(String expression, Set<String> visited, int depth, Project project) {
    return null;
  }
}
