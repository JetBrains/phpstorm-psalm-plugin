<?php
class Bar{}
/**
 * @psalm-param class-string<Foo>|class-string<Bar> $param
 * @psalm-param class-string<Foo>|<weak_warning descr="Duplicate type 'class-string<Foo>'">class-string<Foo></weak_warning> $param1
 * @psalm-param literal-string&callable-string $param1
 * @psalm-param literal-string&<weak_warning descr="Duplicate type 'literal-string'">literal-string</weak_warning> $param1
 */
function f($param, $param1)
{
}


/**
 * @template T
 */
class Foo
{
    /**
     * @template S
     * @psalm-param T|S $a
     * @psalm-param T|<weak_warning descr="Duplicate type 'T'">T</weak_warning> $a
     */
    public function f($a)
    {
    }
}