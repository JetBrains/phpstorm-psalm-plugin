<?php

/**
 * @template T
 */
class Map
{
    /**
     * @param array<string, T>
     */
    public function __construct(array $data = []) {}
    /**
     * @return T
     */
    public function get(string $key) {}
}
$map = new Map([new Exception(),new Exception()]);
<type value="Exception|mixed">$map->get('a')</type>;


/**
 * @template T
 */
class MapList
{
    /**
     * @param list<T>
     */
    public function __construct(array $data = []) {}
    /**
     * @return T
     */
    public function get(string $key) {}
}
$map = new MapList([new Exception(),new Exception()]);
<type value="Exception|mixed">$map->get('a')</type>;

/**
 * @template T
 */
class MapSame
{
    /**
     * @param T
     */
    public function __construct(array $data = []) {}
    /**
     * @return T
     */
    public function get(string $key) {}
}
$map = new MapSame([new Exception(),new Exception()]);
<type value="Exception[]|mixed">$map->get('a')</type>;