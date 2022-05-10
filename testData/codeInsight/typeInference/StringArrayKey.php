<?php

/**
 * @param array<string, string> $param
 */
function f($param){
    foreach ($param as $key=>$value){
        return $key;
    }

}

<type value="null|string">$f</type> = f();
