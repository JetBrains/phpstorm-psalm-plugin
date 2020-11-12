<?php
namespace K;
/**
 * @param array<int, Closure> $arr
 * @param callable():int $arr
 */
function takesArray(array $arr)
{
    if ($arr) {
        echo $arr[1](1);
    }
}

