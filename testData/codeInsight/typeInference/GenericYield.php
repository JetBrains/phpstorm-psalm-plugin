<?php
/**
 * @template-covariant TValue
 * @psalm-yield TValue
 */
interface Promise
{
    /**
     * @return TValue
     */
    function f();
}

class Foo{}

/**
 * @return Promise<Foo>
 */
function f(){}
class Bar{}

/**
 * @template TKey
 * @template TValue
 * @psalm-yield TValue
 */
class Holder {

}
class DD {
    /**
     * @var Holder<Foo, Bar>
     */
    private $a;
    /**
     * @param Promise<Bar> $f
     * @return Foo|Generator
     */
    function ff($f) {
        <type value="Foo">yield f()</type>;
        <type value="Bar">yield $f</type>;
        <type value="Bar">yield $this->a</type>;
    }

}