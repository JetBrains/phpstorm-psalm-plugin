<?php

/**
 * @template T
 */
interface Base
{
    /**
     * @return T
     */
    function f();
}

class P
{
}

/**
 * @template-implements Base<P>
 */
class Child implements Base
{
}

$child = new Child();
<type value="P">$child->f()</type>;

/**
 * @implements Base<P>
 */
class Child1 implements Base
{
}

$child1 = new Child1();
<type value="P">$child->f()</type>;