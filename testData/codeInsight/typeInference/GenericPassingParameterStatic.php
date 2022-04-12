<?php

/**
 * @template TValue
 */
class C {
    /**
     * @return static<array<TValue>>
     */
    public function crossJoin($lists) { }
    /**
     * @return TValue
     */
    public function first() { }
}


/**
 * @param \C<\Exception> $param
 */
function temp($param){
    <type value="Exception[]|mixed">$param->crossJoin()->first()</type>;
}
