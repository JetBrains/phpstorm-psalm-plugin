<?php
namespace A;
class Foo{

}

namespace B;
use A\Foo;
/**
 * @param class-string<Foo> $class
 * @param string $class1
 * @param string $class2
 */
function loadWithDirectUnwrap(string $class, string $class1, $class2) {
    <type value="\A\Foo|mixed">new $class()</type>;
    <type value="mixed">new $class1()</type>;
    <type value="mixed">new $class2()</type>;
}