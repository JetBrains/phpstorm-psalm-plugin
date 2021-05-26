<?php

function f(){
    /** @var array{name?: Exception, age: int} $test */
    $test = [];
    $test['<caret>'];
}