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
 * @param  iterable<Exception>  $param
 */
function f($c, array $param) {
    <type value="Exception|mixed">$c->f($param)</type>
}
