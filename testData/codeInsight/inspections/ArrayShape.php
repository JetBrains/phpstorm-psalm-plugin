<?php

function f(){
    /** @var array{name?: Exception, age: int} $test */
    $test = [];
    /** <weak_warning descr="@var tag specifies the type already inferred from source code">@var array $test1</weak_warning> */
    $test1 = [];
}