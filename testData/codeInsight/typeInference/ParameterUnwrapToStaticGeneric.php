<?php


/**
 * @template T
 */
class A {

    /**
     * @template TValue
     * @param iterable<TValue> $a
     * @return static<TValue>
     */
    public static function make($a) {}

    /**
     * @template TValue
     * @param TValue $a
     * @return static<TValue>
     */
    public static function makeFromValue($a) {}
}


/**
 * @template T
 */
class B extends A {
    /**
     * @return T
     */
    public function getValue()
    {

    }
}

class Foo {}


function f() {
    <type value="string">B::make(["a"])->getValue()</type>;
    <type value="Foo">B::make([new Foo])->getValue()</type>;
    <type value="Foo">B::makeFromValue(new Foo)->getValue()</type>;
}
