<?php
/**
 * @template T
 */
class C {
    /**
     * @template TValue
     * @param  iterable<TValue>  $param
     * @return TValue
     */
    public function unwrapIterable($param) {}

    /**
     * @template TValue
     * @param  C<TValue>  $param
     * @return TValue
     */
    public function unwrapClass($param) {}
}
/**
 * @param C<stdClass> $base
 * @param iterable<Exception> $param1
 * @param C<Exception> $param2
 */
function f($base, $param1, $param2) {
    <type value="Exception|mixed">$base->unwrapIterable($param1)</type>;
    <type value="mixed">$base->unwrapIterable($param2)</type>;
    <type value="mixed">$base->unwrapClass($param1)</type>;
    <type value="Exception|mixed">$base->unwrapClass($param2)</type>;
}