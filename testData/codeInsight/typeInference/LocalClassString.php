<?php

class Foo{

}
/**
 * @param class-string<Foo> $class
 * @param class-string<Foo<X>> $class1
 */
function loadWithDirectUnwrap(string $class, string $class1) {
    <type value="Foo|mixed">new $class()</type>;
    <type value="Foo|mixed">new $class1()</type>;
}