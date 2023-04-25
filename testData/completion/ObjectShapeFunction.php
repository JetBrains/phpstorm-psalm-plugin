<?php

/** @return  object{ foo: Foo, bar: object{ baz: string } }*/
function foo() {

}

class Foo {

}
foo()->bar-><caret>
