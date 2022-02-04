<?php

function f(){
    /** @var array<array{person: array{name?: Exception, age: int}}> $test */
    $test = [];
    $test[$a]["person"]['<caret>'];
}