<?php

namespace X;

/**
 * @template TKey
 * @template TValue
 */
class AA {
    /**
     * @template TMapValue
     *
     * @param Closure(TValue, TKey): TMapValue $callback
     * @return static<TKey, TMapValue>
     */
    public function map(callable $callback)
    {
        $keys = array_keys($this->items);

        $items = array_map($callback, $this->items, $keys);

        return new static(array_combine($keys, $items));
    }

    /**
     * @return TValue
     */
    public function first()
    {

    }
}

<type value="BB">$first</type> = (new AA)->map(function ($id, $aa) {
    return new \BB();
})->first();

<type value="BB">$first</type> = collect()->map(function ($id, $aa) {
    return new \BB();
})->first();
