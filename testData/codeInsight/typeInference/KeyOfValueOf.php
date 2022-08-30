<?php

enum MyEnum: string {}

class A
{
  const CC = [1,2,3];
  /**
   * @param value-of<self::CC> $a
   * @param key-of<self::CC> $b
   * @param key-of<self::DD> $c
   * @param value-of<self::DD> $d
   * @param value-of<MyEnum> $f
   */
  function hello($a, $b, $c, $d, $f)
  {
    <type value="int">$a</type>;
    <type value="int">$b</type>;
    <type value="int|string">$c</type>;
    <type value="mixed">$d</type>;
    <type value="string">$f</type>;
  }
}


