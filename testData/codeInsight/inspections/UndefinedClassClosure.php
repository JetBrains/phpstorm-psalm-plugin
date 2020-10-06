<?php
namespace K;
/**
 * @param array<int, Closure> $arr
 */
function takesArray(array $arr)
{
    if ($arr) {
        echo $arr[1](1);
    }
}

