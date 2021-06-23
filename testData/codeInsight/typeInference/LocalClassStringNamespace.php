<?php
namespace A;
class Foo{

}

namespace B;
use A\Foo;
/**
 * @param class-string<Foo> $class
 */
function loadWithDirectUnwrap(string $class, string $class1) {
    <type value="\A\Foo|mixed">new $class()</type>;
}