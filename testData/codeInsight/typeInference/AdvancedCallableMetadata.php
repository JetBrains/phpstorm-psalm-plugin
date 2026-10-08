<?php

/** @return \Closure(string &$optional=, int &...$rest): bool */
function create_metadata_callback(): Closure
{
}

$callback = <type value="\Closure<#ᤓ&optional=ᤓ\string,#ᤓ...&restᤓ\int[],\bool>">create_metadata_callback()</type>;


class VariadicItem {}

/** @template T */
class VariadicFactory {
    /** @return callable(T ...$values): T */
    public function create(): callable {}
}

/** @var VariadicFactory<VariadicItem> $factory */
$factory = new VariadicFactory();
$variadic = <type value="callable<#ᤓ...valuesᤓ#ᢐT.[],#ᢐT.>|\callable<#ᤓ...valuesᤓ\VariadicItem[],\VariadicItem>">$factory->create()</type>;
<type value="VariadicItem">$variadic(new VariadicItem(), new VariadicItem())</type>;

/**
 * @template T
 * @param callable(T ...$values): void $callback
 * @return T
 */
function extract_variadic_parameter_type(callable $callback) {}

/** @var callable(VariadicItem ...$values): void $documentedVariadic */
<type value="VariadicItem">extract_variadic_parameter_type($documentedVariadic)</type>;
<type value="VariadicItem">extract_variadic_parameter_type(static function (VariadicItem ...$values): void {})</type>;
/** @var callable(VariadicItem[] ...$values): void $arrayVariadic */
<type value="VariadicItem[]">extract_variadic_parameter_type($arrayVariadic)</type>;
