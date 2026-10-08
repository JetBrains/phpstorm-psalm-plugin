<?php

/**
 * @return Closure(array $items, int|string $value, int $a) : bool
 */
function a(): Closure {

}



$b = a();
$b(<warning descr="Parameter type 'int' is not compatible with 'array'">1</warning>,2,<warning descr="Parameter type 'array' is not compatible with 'int'">[]</warning>);
$b([],"a");
$b([],<warning descr="Parameter type 'array' is not compatible with 'int|string'">[]</warning>,3,4);
$b(<warning descr="Parameter type 'int' is not compatible with 'array'">ff()</warning>,3,4);
$b();

$b(a: 3, value: "item", items: []);
$b(
    a: <warning descr="Parameter type 'array' is not compatible with 'int'">[]</warning>,
    value: <warning descr="Parameter type 'array' is not compatible with 'int|string'">[]</warning>,
    items: <warning descr="Parameter type 'int' is not compatible with 'array'">1</warning>
);


/** @template T */
class VariadicFactory {
    /** @return callable(T ...$values): void */
    public function create(): callable {}
}

/** @var VariadicFactory<int> $factory */
$factory = new VariadicFactory();
$variadic = $factory->create();
$variadic(1, 2, 3);
$variadic(...[1, 2]);
$variadic(1, <warning descr="Parameter type 'array' is not compatible with 'int'">[]</warning>);
