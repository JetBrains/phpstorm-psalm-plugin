<?php

class C{
    const A_1 = 1;
    const A_2 = "a";
    const B_2 = 1.0;
}

function f($a, $b)
{
    /** @psalm-var C::A_* $b*/
    echo <type value="int|string">$b</type>;

    /** @psalm-var C::* $b1*/
    echo <type value="float|int|string">$b1</type>;
}