<?php

/**
 * @template T
 * @psalm-param T $t
 * @return T
 */
function foo($t){
    return $t;
}
<selection>$f</selection> = foo(1);