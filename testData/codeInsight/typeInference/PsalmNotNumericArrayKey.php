<?php

/**
 * @param array<float, int> $a
 */
function foo($a)
{
    foreach ($a as $key => $value) {
        <type value="int|string">$key</type>;
    }
}