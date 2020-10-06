<?php
/**
 * @psalm-param T $t
 * @psalm-return T
 */
function mirror($t) {
    return $t;
}

$a = 5;
<selection>$b</selection> = mirror($a);