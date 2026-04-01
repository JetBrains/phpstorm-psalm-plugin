<?php

enum StringMapper: string
{
    case Foo = 'foo';
    case Bar = 'bar';

    /** @return array<value-of<self>, int> */
    public static function keyMap(): array
    {
        $mapping = [];
        foreach (self::cases() as $index => $case) {
            $mapping[$case->value] = $index;
        }

        return $mapping;
    }

    /** @return array<int, value-of<self>> */
    public static function valueMap(): array
    {
        $mapping = [];
        foreach (self::cases() as $index => $case) {
            $mapping[$index] = $case->value;
        }

        return $mapping;
    }

    /** @return array<value-of<self>, value-of<self>> */
    public static function bidirectionalMap(): array
    {
        $mapping = [];
        foreach (self::cases() as $case) {
            $mapping[$case->value] = $case->value;
        }

        return $mapping;
    }
}

<type value="int[]">StringMapper::keyMap()</type>;
<type value="string[]">StringMapper::valueMap()</type>;
<type value="string[]">StringMapper::bidirectionalMap()</type>;
