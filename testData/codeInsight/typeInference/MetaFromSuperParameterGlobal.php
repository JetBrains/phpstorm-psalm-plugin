<?php
class A{}
class B{}


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
