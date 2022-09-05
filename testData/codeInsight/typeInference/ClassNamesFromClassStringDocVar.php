<?php

class HelloWorld
{
	public static function sayHello(): int
	{
		return 'Hello';
	}
}

function foo(): void {
	/** @var class-string<HelloWorld> $hello */
	$hello = f();

	<type value="int">$hello::sayHello()</type>;
	<type value="">$hello::undefinedMethod()</type>;
}


/**
 * @template T
 * @param class-string<T> $s
 * @return T
 */
function ff(string $s)
{
    return new $s;
}

<type value="">ff()</type>; // no 'T'
