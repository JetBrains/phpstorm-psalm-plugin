<?php

enum IntMapper: int
{
    case One = 1;
    case Two = 2;

    /** @return value-of<self>[] */
    public static function values(): array
    {
        return [self::One->value, self::Two->value];
    }

    /** @return list<value-of<self>> */
    public static function valuesList(): array
    {
        return [self::One->value, self::Two->value];
    }
}

<type value="int[]">IntMapper::values()</type>;
<type value="int[]">IntMapper::valuesList()</type>;
