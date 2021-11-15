<?php

class HelloWorld
{
    public static $sProperty;
    public static function sayHello(): int
    {
        return 'Hello';
    }
}

/**
 * @param class-string<HelloWorld> $hello
 */
function foo($hello): void {
    $hello::<caret>
}
