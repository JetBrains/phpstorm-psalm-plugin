<?php

/**
 * @param array<string> $param
 */
function f($param){
    foreach ($param as $key=>$value){
        return $key;
    }

}

<type value="null|int|string">$f</type> = f();
