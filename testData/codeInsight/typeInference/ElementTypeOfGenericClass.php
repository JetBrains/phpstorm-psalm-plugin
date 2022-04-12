<?php

/**
 * @template TKey of array-key
 * @template TValue
 */
class B {
    /**
     * @return array<TKey, TValue>
     */
    public function all()
    {

    }
}

/**
 * @template TKey of array-key
 * @template TValue
 */
class A {
    /**
     * @return B<TKey, TValue>
     */
    public function lazy()
    {

    }
}

/**
 * @param A<int, Exception> $param
 */
function f2($param){
    <type value="Exception|mixed">$param->lazy()->all()[0]</type>;
}
