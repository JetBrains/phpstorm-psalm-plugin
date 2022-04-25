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
    public function f($param) {
    }
}
/**
 * @param  C  $c
 * @param  C<stdClass>  $c1
 * @param  iterable<Exception>  $param
 */
function f($c, $c1, array $param) {
    <type value="Exception|mixed">$c->f($param)</type>
    <type value="Exception|mixed">$c1->f($param)</type>
}
