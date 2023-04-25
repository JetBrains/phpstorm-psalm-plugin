<?php

class Foo {
    /**
     * @var object{name: string, foo : object {name: string, age : int}} $a
     */
    public $a;

    public function b(): void
    {
        $this->a-><caret>
    }

}

