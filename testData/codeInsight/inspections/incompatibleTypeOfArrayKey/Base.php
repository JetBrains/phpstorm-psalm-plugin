<?php

/**
 * @psalm-param array<int, string> $arr
 */
function f1(array $arr) : void {
  echo $arr[<warning descr="Incompatible type of array key, expecting 'int'">"a"</warning>];
}

/**
 * @psalm-param array<string, int> $arr
 */
function f2(array $arr) : void {
  $arr[<warning descr="Incompatible type of array key, expecting 'string'">1</warning>] = 1;
}

/**
 * @psalm-param array<string, int> $arr
 */
function f3(array $arr, int $key) : void {
  $arr[<warning descr="Incompatible type of array key, expecting 'string'">$key</warning>] = 1;
}

/**
 * @psalm-param array<int, string> $arr
 */
function n1(array $arr, $key) : void {
  $arr[$key] = 1;
}

/**
 * @psalm-param array<int, string> $arr
 */
function n2(array $arr, int|string $key) : void {
  $arr[$key] = 1;
}

/**
 * @psalm-param array<int, string> $arr
 */
function n3(array $arr, mixed $key) : void {
  $arr[$key] = 1;
}

/**
 * @psalm-param array<string, string> $arr
 */
function n4(array $arr) : void {
  $arr["1"] = 1;
}

/**
 * @psalm-param array<int, string> $arr
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

/**
 * @psalm-param array<int, string> $arr
 */
function n7(array $arr) : void {
  $arr["0"] = 1;
}

/**
 * @psalm-param array<float, string> $arr
 */
function n8(array $arr) : void {
  $arr["0"] = 1;
}

/**
 * @psalm-param array<string, string> $arr
 */
function n9(array $arr) : void {
  $arr[undef_func123()] = 1;
}