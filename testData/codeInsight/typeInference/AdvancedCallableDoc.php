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

/**
 * @param Closure(): Foo $docClosure
 * @param callable(): Foo $docCallable
 */
function extractClosureInferred($docClosure, $docCallable) {
    <type value="Bar">$aa</type> = extractClosureReturnTemplate($docClosure)->aa();
    <type value="Bar">$aa</type> = extractCallableReturnTemplate($docClosure)->aa();
    <type value="Bar">$aa</type> = extractClosureReturnTemplate($docCallable)->aa();
    <type value="Bar">$aa</type> = extractCallableReturnTemplate($docCallable)->aa();
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
function extractClosureReturnTemplateWrapped(callable $key1){}

/**
 * @param Closure() : Bar $closure
 */
function extractAndWrapClosureInferred($closure) {
    <type value="Bar|mixed">$aa</type> = extractClosureReturnTemplateWrapped($closure)->aa();
}