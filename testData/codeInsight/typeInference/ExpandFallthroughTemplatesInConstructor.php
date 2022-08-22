<?php

/**
 * @template T
 */
class Foo
{
    /**
     * @param T $a
     */
    public function __construct(private mixed $a) { }

    /**
     * @return T
     */
    public function get()
    {
        return $this->a;
    }
}

/**
 * @template T
 * @template-extends Foo<T>
 */
class Bar extends Foo {}

/**
 * @template T
 * @template-extends Bar<T>
 */
class Baz extends Bar {}

$foo = new Foo(new DateTime());
$bar = new Bar(new DateTime());
$baz = new Baz(new DateTime());

<type value="mixed|DateTime">$foo->get()</type>;
<type value="DateTime|mixed">$bar->get()</type>;
<type value="DateTime|mixed">$baz->get()</type>;
