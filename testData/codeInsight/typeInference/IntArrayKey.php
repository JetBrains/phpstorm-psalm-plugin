<?php

/**
 * @param array<int, string> $param
 */
function f($param){
    foreach ($param as $key=>$value){
        return $key;
    }

}

<selection>$f</selection> = f();
