<?php
/**
 * @template T of <warning descr="Undefined class 'Foo'">Foo</warning>
 * @template T1
 * @template T2
 * @template
 * @psalm-param T $t
 * @psalm-param T1 $t
 * @psalm-param T2 $t
 * @psalm-param <warning descr="Undefined class 'T3'">T3</warning> $t
 * @return array<<T1, <warning descr="Undefined class 'T3'">T3</warning>>, T>
 */
function makeArray($t) {
    return [$t];
}


/**
 * @template T
 * @psalm-template T1
 * @template-covariant T33
 */
class MyContainer {
    /** @var T */
    private $value;

    /** @var T33 */
    private $value1;

    /** @param T $value */
    public function __construct($value) {
        $this->value = $value;
    }

    /** @return T */
    public function getValue() {
        return $this->value;
    }

    /**
     * @template T22
     * @return T1
     */
    public function getValue1() {
        /** @var T22|<warning descr="Undefined class 'T23'">T23</warning> $value */
        echo $value;
        return $this->value;
    }
}


/**
 * @template T3
 */
class F {
    /**
     * @template T
     */
    function f($param) {
        new class {
            public function ff() {
                /**
                 * @template T1
                 * @psalm-param T $a
                 * @psalm-param T3 $a
                 */
                $a = function ($x) {
                    /** @psalm-param T1 $x */
                    $x;

                    /** @psalm-param T $x */
                    $x;

                    /** @psalm-param T3 $x */
                    $x;
                };
            }
        };
    }
}