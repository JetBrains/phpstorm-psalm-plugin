<?php
class B {}
/**
 * @param <warning descr="Undefined class 'A'">A</warning>[]
 * @param array<<warning descr="Undefined class 'A'">A</warning>>
 * @param B[]
 * @param array<B>
 */
function f()
{
    echo 'Hello World!';
}