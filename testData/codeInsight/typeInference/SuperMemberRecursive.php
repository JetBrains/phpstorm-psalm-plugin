<?php

/**
 * @template T
 */
interface I
{
    /**
     * @return T
     */
    public function m();

    /**
     * @return T
     */
    public function m1();
}

/**
 * @implements I<B>
 */
interface Child extends Child
{

    public function m()
    {
    }

    /**
     * @return A
     */
    public function m1()
    {
    }
}

<type value="">(new Child())->m()</type>;
<type value="A">(new Child())->m1()</type>;