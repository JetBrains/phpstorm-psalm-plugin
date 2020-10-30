<?php

/**
 * @psalm-return no-return
 */
function f()
{
}

/**
 * @return never-returns
 */
function f1()
{
}

class A {
    /**
     * @psalm-return never-return
     */
    function m()
    {
    }
}


function g(A $a){
    if ($a) {
        f();
        <warning descr="Unreachable statement">echo "a";</warning>
    }

    if ($a) {
        f1();
        <warning descr="Unreachable statement">echo "a";</warning>
    }

    if ($a) {
        $a->m();
        <warning descr="Unreachable statement">echo "a";</warning>
    }

    if ($a) {
        f2();
        echo "a";
    }
}