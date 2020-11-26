<?php

/**
 * @template T
 * @psalm-param T $t
 * @return T
 */
function foo($t){
    return $t;
}
<type value="int|mixed">$f</type> = foo(1);