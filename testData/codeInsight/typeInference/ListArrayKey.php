<?php

/**
 * @param list<string> $param
 */
function f($param){
    foreach ($param as $key=>$value){
        return $key;
    }

}

/**
 * @param array<string> $param
 */
function f1($param){
    foreach ($param as $key=>$value){
        return $key;
    }

}

<type value="null|int">$f</type> = f();
<type value="null|string|int">$f1</type> = f1();
