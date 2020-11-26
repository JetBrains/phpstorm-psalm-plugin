<?php

class C{
    const A_1 = 1;
    const A_2 = 2;
}

function f($a, $b)
{
    /** @psalm-var C::A_1 $b*/
    echo <type value="int">$b</type>;
}