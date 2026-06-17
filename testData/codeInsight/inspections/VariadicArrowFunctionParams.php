<?php

// arrow function with variadic int: a single int argument should be accepted
$f1 = fn (int ...$args) => null;
$f1(5);

// arrow function with variadic string: each argument must be `string`
$coalesce = fn (string ...$keys) => implode(',', $keys);
$coalesce('config.shop.php.memory_limit', 'deployment.php.memory_limit');

// regression guard: `int[]` for `int ...$x` arrow function must still warn
$f2 = fn (int ...$x) => null;
$f2(<warning descr="Parameter type 'int[]' is not compatible with 'int'">[1, 2]</warning>);
