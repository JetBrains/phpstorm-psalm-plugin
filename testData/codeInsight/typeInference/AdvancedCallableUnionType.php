<?php
use Closure as ClosureAlias;
class Foo {
}

/**
 * @return int|(ClosureAlias(bool, int|string, $a int) : int)|Foo
 */
function a1(): Closure {

}
function b() {
    return a();
}

$b1 = a1();
<type value="int|mixed">$b1()</type>;