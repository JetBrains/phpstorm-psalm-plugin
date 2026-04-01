<?php

enum StringMapper: string
{
    case Foo = 'foo';
    case Bar = 'bar';

    /** @return array<value-of<self>, int> */
    public static function keyMap(): array
    {
        return [];
    }

    /** @return array<int, value-of<self>> */
    public static function valueMap(): array
    {
        return [];
    }

    /** @return array<value-of<self>, value-of<self>> */
    public static function bidirectionalMap(): array
    {
        return [];
    }
}
