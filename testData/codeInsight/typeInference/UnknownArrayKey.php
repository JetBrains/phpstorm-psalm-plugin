<?php

/**
 * @param array<string> $param
 */
function f($param){
    foreach ($param as $key=>$value){
        return $key;
    }

}

<selection>$f</selection> = f();
