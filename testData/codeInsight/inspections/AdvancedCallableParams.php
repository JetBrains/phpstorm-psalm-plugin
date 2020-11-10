<?php

/**
 * @return Closure(array, int|string, $a int) : bool
 */
function a(): Closure {

}



$b = a();
$b(<warning descr="Parameter type 'int' is not compatible with 'array'">1</warning>,2,<warning descr="Parameter type 'array' is not compatible with 'int'">[]</warning>);
$b([],"a");
$b([],<warning descr="Parameter type 'array' is not compatible with 'int|string'">[]</warning>,3,4);
$b();

