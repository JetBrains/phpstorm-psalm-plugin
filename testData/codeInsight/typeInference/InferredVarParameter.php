<?php
class FF {
    function bar1($foo)
    {

    }
}
class Foo1 extends FF
{
    public function bar()
    {
    }

    function bar1(Foo $foo)
    {
        $new_foo = new $foo();
        <type value="Foo">$new_foo</type>;

    }
}

function useVar(P $p)
{
    $foo = "Foo";
    $bar = "Foo";
    function () use ($foo, $bar, $p) {
        $bar = "Bar";
        <type value="Foo">new $foo()</type>;
        <type value="Bar">new $bar()</type>;
        <type value="P">new $p()</type>;
    }

}



