<?php
/**
 * @template T
 * @psalm-param T $t
 * @psalm-return T
 */
function mirror($t) {
    return $t;
}

$a = 5;
<type value="int">$b</type> = mirror($a);