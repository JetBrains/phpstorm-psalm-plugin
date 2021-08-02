<?php

/**
 * @psalm-param list<string> $arr
 */
function f1(array $arr) : void {
  echo $arr[<warning descr="Incompatible type of array key, expecting 'int'">"a"</warning>];
}

/**
 * @psalm-param list<string> $arr
 */
function f2(array $arr) : void {
  $arr[<warning descr="Incompatible type of array key, expecting 'int'">"a"</warning>] = 1;
}

/**
 * @psalm-param list<string> $arr
 */
function f3(array $arr, string $key) : void {
  $arr[<warning descr="Incompatible type of array key, expecting 'int'">$key</warning>] = 1;
}

/**
 * @psalm-param list<string> $arr
 */
function f4(array $arr) : void {
  $arr[<warning descr="Incompatible type of array key, expecting 'int'">1.0</warning>] = 1;
}

/**
 * @psalm-param list<string> $arr
 */
function f5(array $arr) : void {
  $arr[<warning descr="Incompatible type of array key, expecting 'int'">true</warning>] = 1;
}


/**
 * @psalm-param list<string> $arr
 */
function n1(array $arr, $key) : void {
  $arr[$key] = 1;
}

/**
 * @psalm-param list<string> $arr
 */
function n2(array $arr, int|string $key) : void {
  $arr[$key] = 1;
}

/**
 * @psalm-param list<string> $arr
 */
function n3(array $arr, mixed $key) : void {
  $arr[$key] = 1;
}

/**
 * @psalm-param list<string> $arr
 */
function n4(array $arr) : void {
  $arr["1"] = 1;
}

/**
 * @psalm-param list<string> $arr
 */
function n5(array $arr) : void {
  $arr[1] = 1;
}

/**
 * @psalm-param list<string> $arr
 */
function n6(array $arr) : void {
  $arr[$key] = 1;
}