<?php

/**
 * @var array<int-mask, int> $a
 */
foreach ($a as $key => $value) {
    <type value="int">$key</type>;
}


/**
 * @param array<class-string, int> $a
 */
function foo($a)
{
    foreach ($a as $key => $value) {
        <type value="string">$key</type>;
    }
}

