<?php
/**
 * @psalm-type T
 * @psalm-type T1 = array{name: string, age: int}
 * @psalm-type T2=array{a: int}
 * @psalm-type
 * @psalm-param T $t
 * @psalm-param T1 $t
 * @psalm-param T2 $t
 * @psalm-param <warning descr="Undefined class 'T3'">T3</warning> $t
 * @return array<<T1, <warning descr="Undefined class 'T3'">T3</warning>>, T>
 */
function makeArray($t) {
    return [$t];
}

/**
 * @psalm-import-type T1 from T2 as T3
 * @return array<<warning descr="Undefined class 'T1'">T1</warning>, <warning descr="Undefined class 'T2'">T2</warning>, T3>
 */
function makeArray1($t) {
    return [$t];
}

/**
 * @psalm-import-type T1 from T2
 * @return array<T1, <warning descr="Undefined class 'T2'">T2</warning>, <warning descr="Undefined class 'T3'">T3</warning>>
 */
function makeArray2($t) {
    return [$t];
}