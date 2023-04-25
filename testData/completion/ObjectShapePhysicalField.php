<?php

class Foo {
    /**
     * @var object{name2: string, foo : object {name: string, age : int}} $a
     */
    public $a;

}

$a = new Foo();
$a->a->foo-><caret>