<?php
class Foo {}
function f(){
    /** @var iterable<Foo,Exception> $test */
    $test = [];

    foreach ($test as $key => $e) {
        <selection>$key</selection>;
    }
}