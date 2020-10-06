<?php
/**
 * @template T
 * @template T1
 */
class A {
    /**
     * @param T $t
     * @param T1 $t2
     * @return T&T1
     */
    function mirror($t, $t2) {
        return $t;
    }
}

$a = "a";
$b = 5;
<selection>$c</selection> = (new A())->mirror($a, $b);