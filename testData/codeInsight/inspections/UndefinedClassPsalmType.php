<?php
/**
 * @psalm-type T1 = array{name: string, age: int}
 * */
class A {}
/**
 * @psalm-type T = A
 * @psalm-type TB = <warning descr="Undefined class 'B'">B</warning>
 * @psalm-type T1 = array{name: string, age: int}
 * @psalm-type T2=array{a: int}
 * @psalm-type
 * @psalm-param T $t
 * @psalm-param T1 $t
 * @psalm-param T2 $t
 * @psalm-param TB $t
 * @psalm-param <warning descr="Undefined class 'T3'">T3</warning> $t
 * @return array<<T1, <warning descr="Undefined class 'T3'">T3</warning>>, T>
 */
function makeArray($t) {
    return [$t];
}

/**
 * @psalm-import-type T1 from A as T3
 * @return array<T3, A>
 */
function makeArray1($t) {
    return [$t];
}

/**
 * @psalm-import-type T1 from <warning descr="Undefined class 'B'">B</warning>
 * @return array<T1, <warning descr="Undefined class 'T2'">T2</warning>, <warning descr="Undefined class 'T3'">T3</warning>>
 */
function makeArray2($t) {
    return [$t];
}