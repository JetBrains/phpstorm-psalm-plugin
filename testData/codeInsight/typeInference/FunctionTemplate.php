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

/**
 * @template T
 * @param A<T> $t
 * @return A<T>
 */
function mirror1($t) {
    return $t;
}
class B {}
<type value="A|B">$b</type> = mirror1(new B());

/**
 * @param A<T> $t
 * @return A<T>
 */
function mirrorUnknown($t) {
    return $t;
}
<type value="A">$b</type> = mirrorUnknown(new B());