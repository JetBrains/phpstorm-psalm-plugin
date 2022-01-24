<?php

/**
 * @psalm-param $a array{int, string}
 * @param $b array{"name": int, string}
 * @param $c array{"name": int, 1?: string}
 * @param $d array{"person": array{name: string, age: int}, 1?: string}
 * @psalm-return array{age: int}
 */
function f($a, $b, $c, $d){
    /** @var array{name?: Exception, age: int} $test */
    <type value="array">$test = []</type>;
    <type value="Exception|mixed">$test['name']</type>;
    <type value="int|mixed">$b['name']</type>;
    <type value="int|mixed">$a[0]</type>;
    <type value="string|mixed">$a[1]</type>;
    <type value="string|mixed">$c[1]</type>;
    <type value="string|mixed">$d["person"]["name"]</type>;
    <type value="int|mixed">$d["person"]["age"]</type>;
    <type value="array|mixed">$d["person"]</type>;
}

$f = f();
<type value="int|mixed">$f["age"]</type>;


class C{

}

function f1($param){
    /** @var array<string, C> $ctxA */
    $ctxA = $param->f();
    <type value="C">$ctxA[0]</type>;
    <type value="C">$ctxA[1]</type>;

    /** @var array{string, C} $ctxB */
    $ctxB = $param->f();
    <type value="string|mixed">$ctxB[0]</type>;
    <type value="C|mixed">$ctxB[1]</type>;

    <type value="string|C|mixed">$ctxB[$f]</type>;
}