<?php
class A{}
class B{}
/**
 * @template T
 */
interface I
{
    /**
     * @param A $min
     * @param B $max
     * @return T
     */
    public function rand($min, $max);
}

/**
 * @implements I<A, B>
 */
class c implements I
{
    public function rand($min, $max)
    {
        <type value="A">$min</type>;
        <type value="B">$max</type>;
    }
}
