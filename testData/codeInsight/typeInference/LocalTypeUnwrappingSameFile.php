<?php

class C
{
}

/**
 * @template T
 */
class Bar
{
    /**
     * @psalm-return T
     */
    public function doBaz()
    {
    }
}

class Baz
{
    /**
     * @var Bar<C>
     */
     private $a;

    /**
     * @param Bar<C> $p
     */
    public function doFoo($p): void
    {
        <type value="C">$p->doBaz()</type>;
        <type value="C">$this->a->doBaz()</type>;
    }
}
