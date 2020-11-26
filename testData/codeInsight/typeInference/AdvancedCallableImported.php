<?php
use Closure as ClosureAlias;
class Foo {
}

/**
 * @return ClosureAlias(bool, int|string, $a int) : int|Foo
 */
function a(): Closure {

}

function b() {
    return a();
}

$b = b();
<type value="Foo|int|mixed">$b()</type>;