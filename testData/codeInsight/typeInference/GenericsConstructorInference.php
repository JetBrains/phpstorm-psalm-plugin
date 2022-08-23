<?php

/**
 * @template T
 * @template T1
 */
class Collection{
    /**
     * @param T $item
     * @param int $pos
     * @param T1 $second
     */
    public function __construct($item, $pos, $second)
    {
    }

    /**
     * @return T
     */
    public function first()
    {
    }

    /**
     * @return T1
     */
    public function second(){
    }
}



class ItemA {
}
class ItemB {
}

function a(ItemA $a, ItemB $b){
    $collection = new Collection($a, 0, $b);
    <type value="ItemA">$b</type> = $collection->first();
    <type value="ItemB">$b</type> = $collection->second();

    $collection = new Collection(new ItemB(), 0, new ItemA());
    <type value="ItemB">$b</type> = $collection->first();
    <type value="ItemA">$b</type> = $collection->second();
}