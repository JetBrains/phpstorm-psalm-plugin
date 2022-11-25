<?php

/**
 * @psalm-return Foo
 * @psalm-param $a int description
 * @psalm-var $b array<int, Foo>
 * @psalm-throws Exception
 * @psalm-template T of Foo with description
 * @psalm-template-covariant M of Foo with description
 */
function fo<caret>o(int $a, $b) {

}

class Foo {

}