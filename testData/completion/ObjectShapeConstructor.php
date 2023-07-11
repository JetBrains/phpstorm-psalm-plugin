<?php

class MyClass extends Foo {

    function a() {
        (new MyClass())->n<caret>;
    }
}