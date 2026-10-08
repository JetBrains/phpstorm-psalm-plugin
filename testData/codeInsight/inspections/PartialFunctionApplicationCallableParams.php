<?php

function pfa_callable_params_target(array $items, int|string $value, int ...$rest): bool
{
    return $items !== [] && $value !== '' && $rest !== [];
}

$partial = pfa_callable_params_target(...);
$partial(
    value: <warning descr="Parameter type 'array' is not compatible with 'int|string'">[]</warning>,
    items: <warning descr="Parameter type 'int' is not compatible with 'array'">1</warning>,
    rest: <warning descr="Parameter type 'array' is not compatible with 'int'">[]</warning>,
);

$partial([], 'value', 1, <warning descr="Parameter type 'array' is not compatible with 'int'">[]</warning>);
