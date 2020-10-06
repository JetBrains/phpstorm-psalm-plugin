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
<selection>$b</selection> = mirror($a);