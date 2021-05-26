<?php

/**
 * @psalm-param $a array{int, string}
 * @param $b array{"name": int, string}
 * @param $c array{"name": int, 1?: string}
 * @psalm-return array{age: int}
 */
function f($a, $b, $c){
    /** @var array{name?: Exception, age: int} $test */
    <type value="array">$test = []</type>;
    <type value="Exception|mixed">$test['name']</type>;
    <type value="int|mixed">$b['name']</type>;
    <type value="int|mixed">$a[0]</type>;
    <type value="string|mixed">$a[1]</type>;
    <type value="string|mixed">$c[1]</type>;
}

$f = f();
<type value="int|mixed">$f["age"]</type>;