<?php

/**
 * @param array<string> $param
 */
function f($param){
    foreach ($param as $key=>$value){
        return $key;
    }

}

<type value="void|int|string">$f</type> = f();
