<?php
/**
 * @psalm-template TKey of array-key
 * @psalm-template T
 */
class ArrayCollection {
    /**
     * @psalm-var array<TKey,T>
     */
    private $elements;
    public function partition() {
        foreach ($this->elements as $key => $element) {
            <type value="int|string">$key</type>;
            <type value="mixed">$element</type>;
        }
    }
}
