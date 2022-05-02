<?php
class Foo {
}

/**
 * @return callable(bool, int|string, $a int) : (int|Foo)
 */
function a(): Closure {

}

function b() {
    return a();
}

$b = b();
<type value="Foo|int|mixed">$b()</type>;