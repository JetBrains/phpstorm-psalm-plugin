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
<type value="int|mixed">$b</type> = mirror($a);