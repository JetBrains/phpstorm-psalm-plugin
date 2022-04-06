<?php
class Foo{
    public function f() {

    }
}


/**
 * @psalm-return Promise<Foo>
 */
function f1(){}
/**
 * @template TValue
 * @psalm-yield TValue
 */
interface Promise
{

}


/**
 * @template-covariant TValue
 * @psalm-yield TValue
 */
interface Promise
{
    /**
     * @return TValue
     */
    function ff();
}

<type value="Foo|mixed">f1()->ff()</type>;
