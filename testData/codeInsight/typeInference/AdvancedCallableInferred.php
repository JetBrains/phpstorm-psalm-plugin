<?php

/**
 * @template TValue of array-key
 *
 * @param  Closure(): TValue  $key1
 * @return TValue
 */
function extractClosureReturnTemplate(callable $key1){}
/**
 * @template TValue of array-key
 *
 * @param  callable(): TValue  $key1
 * @return TValue
 */
function extractCallableReturnTemplate(callable $key1){}

/**
 * @template TValue of array-key
 *
 * @param  Closure(TValue): int  $key1
 * @return TValue
 */
function extractClosureParameterTemplate(callable $key1){}


class Bar {}
class Foo {
    public function aa(): Bar{}
}

function extractClosureInferred() {
    <type value="Bar">$aa</type> = extractClosureReturnTemplate(function () {
        return new Foo;
    })->aa();

    $c = fn() => new Foo;
    <type value="Bar">$x</type> = extractClosureReturnTemplate($c)->aa();
}

function extractCallableInferred(callable $key1){
    <type value="Bar">$aa</type> = extractCallableReturnTemplate(function () {
        return new Foo;
    })->aa();

    $c = fn() => new Foo;
    <type value="Bar">$x</type> = extractCallableReturnTemplate($c)->aa();
}


function extractClosureInferredParameter() {
    <type value="Bar">$aa</type> = extractClosureParameterTemplate(function (Foo $a) {
        return 1 + 2;
    })->aa();
}



/**
 * @template T
 */
class FooTemplated {
    /**
     * @return T
     */
    public function aa(){}
}



/**
 * @template TValue of array-key
 *
 * @param  Closure(): TValue $key1
 * @return FooTemplated<TValue>
 */
function extractClosureReturnTemplateWrapping(callable $key1){}

function extractAndWrapClosureInferred(A $dummy) {
    $key1 = function () {
        return new Bar;
    };
    <type value="Bar|mixed">$aa</type> = extractClosureReturnTemplateWrapping($key1)->aa();
}