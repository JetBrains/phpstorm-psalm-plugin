<?php
namespace N;
/**
 * @param  \N1\Collection<int, \Exception>  $param
 */
function f($param) {
    <type value="int|mixed">$param->flip()->getValue()</type>;
    <type value="int|mixed">$param->flip2()->getValue()</type>;
}


namespace N1;
/**
 * @template TKey of array-key
 * @template TValue
 */
class Collection
{
    /**
     * @return static<TValue, TKey>
     */
    public function flip()
    {
        return new static(array_flip($this->items));
    }

    /**
     * @return Collection<TValue, TKey>
     */
    public function flip2()
    {
        return new static(array_flip($this->items));
    }

    /**
     * @return TValue
     */
    public function getValue()
    {
    }
}
