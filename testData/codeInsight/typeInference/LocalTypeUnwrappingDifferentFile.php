<?php

class Baz
{
    /**
     * @param Bar<C> $p
     */
    public function doFoo($p): void
    {
        <type value="C">$p->doBaz()</type>;
    }
}
