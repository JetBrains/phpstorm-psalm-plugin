<?php declare(strict_types = 1);


/**
 * @var HelloWorld::FOO_*
 */
class HelloWorld
{
    public const FOO_START = 1;
    public const FOO_END = 1;
    public const <weak_warning descr="Unused element: 'BAR_END'">BAR_END</weak_warning> = 1;
}

/**
 * @var HelloWorld1::*
 */
class HelloWorld1
{
    public const FOO_START = 1;
    public const FOO_END = 1;
    public const BAR_END = 1;
}

class <weak_warning descr="Unused element: 'HelloWorld2'">HelloWorld2</weak_warning>
{
    public const <weak_warning descr="Unused element: 'FOO_START'">FOO_START</weak_warning> = 1;
    public const <weak_warning descr="Unused element: 'FOO_END'">FOO_END</weak_warning> = 1;
    public const <weak_warning descr="Unused element: 'BAR_END'">BAR_END</weak_warning> = 1;
}
