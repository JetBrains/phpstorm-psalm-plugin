<?php

class MyTest
{
  private const MY_FIRST_KEY = 'my_first_key';
  private const MY_SECOND_KEY = 'my_second_key';
  private const MY_THIRD_KEY = 'my_third_key';

  private const MY_ARRAY = [
    'a' => self::MY_FIRST_KEY,
    'b' => self::MY_SECOND_KEY,
    'c' => self::MY_THIRD_KEY,

  ];


  /**
   * @param key-of<self::MY_ARRAY> $key
   * @return value-of<self::MY_ARRAY>
   */
  public static function getSpecificValue($key): string
  {
    if ($key == '<caret>')

    }
}