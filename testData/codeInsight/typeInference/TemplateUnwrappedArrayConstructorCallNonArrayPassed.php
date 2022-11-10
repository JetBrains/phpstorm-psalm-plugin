<?php

/**
 * @template T
 */
class MapList
{
    /**
     * @param list<list<T>>
     */
    public function __construct(array $data = []) {}
    /**
     * @return T
     */
    public function get(string $key) {}
}

$map1 = new MapList(new A());
// no StringIndexOutOfBoundsException exception
<type value="mixed">$map1->get('a')</type>;

$map2 = new MapList([[new A()]]);
<type value="A">$map2->get('a')</type>;