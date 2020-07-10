<?php

class C{
    const A_1 = 1;
    const A_2 = "a";
    const B_2 = 1.0;
}

function f($a, $b)
{
    /** @psalm-var C::A_* $b*/
    echo <selection>$b</selection>;
}