<?php

// variadic array: each argument must be `array`; `[1, 2]` is `int[]` which is an array
$cb1 = function (array ...$arrs): void {};
$cb1([1, 2]);

// variadic string: each argument must be `string`
$cb2 = function (string ...$str): void {
    foreach ($str as $s) echo $s;
};
$cb2('A', 'B', 'C');

// long-form closure with variadic int: each argument must be `int`
$cb3 = function (int ...$nums): int {
    return array_sum($nums);
};
$cb3(1, 2, 3);

// regression guard: `int[]` for `int ...$x` must still warn (element-type comparison)
$cb4 = function (int ...$x): void {};
$cb4(<warning descr="Parameter type 'int[]' is not compatible with 'int'">[1, 2]</warning>);
