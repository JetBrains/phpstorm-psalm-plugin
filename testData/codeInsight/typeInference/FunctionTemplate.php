<?php
/**
 * @template T
 * @param T $t
 * @return T
 */
function mirror($t) {
    return $t;
}

$a = 5;
<type value="int|mixed">$b</type> = mirror($a);