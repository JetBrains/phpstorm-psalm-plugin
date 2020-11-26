<?php
/**
 * @psalm-param T $t
 * @psalm-return T
 */
function mirror($t) {
    return $t;
}

$a = 5;
<type value="T">$b</type> = mirror($a);