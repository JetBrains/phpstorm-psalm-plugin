<?php

function f(){
    /** @var array{
     *   person: array{name?: Exception
     *                ,age: int}
     *   } $test
     */
    $test = [];
    $test["person"]['<caret>'];
}