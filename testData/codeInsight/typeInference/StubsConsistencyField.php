<?php
namespace x;
class A {
    /**
     * @psalm-var int $f
     */
    public string $f;
}

<type value="int|string">(new A())->f</type>;