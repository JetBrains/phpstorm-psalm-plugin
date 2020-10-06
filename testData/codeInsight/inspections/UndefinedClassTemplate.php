<?php
/**
 * @template T of Foo
 * @template T1
 * @template T2
 * @template
 * @psalm-param T $t
 * @psalm-param T1 $t
 * @psalm-param T2 $t
 * @psalm-param <warning descr="Undefined class 'T3'">T3</warning> $t
 * @return array<<T1, <warning descr="Undefined class 'T3'">T3</warning>>, T>
 */
function makeArray($t) {
    return [$t];
}