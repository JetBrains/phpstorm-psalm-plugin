<?php

/**
 * @param array<int, string> $param
 */
function f($param){
    foreach ($param as $key=>$value){
        return $key;
    }

}

<type value="null|int">$f</type> = f();
