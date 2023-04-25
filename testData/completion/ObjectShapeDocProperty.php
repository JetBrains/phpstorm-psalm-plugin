<?php

/**
 * @property object{name: string, foo : object {name: string, age : int}} $prop
 */
class Foo {
    public function b(): void
    {
        $this->prop-><caret>
    }
}


