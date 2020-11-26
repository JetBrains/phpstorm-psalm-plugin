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
<type value="int|mixed|string">$c</type> = (new A())->mirror($a, $b);