<?php
namespace F;
class Offer{
    public function myMethod()
    {

    }
}

/**
 * @template T
 */
final class Identifiers
{
    private $iterator;


    /**
     * @param class-string<T> $className
     *
     * @return static<T>
     */
    public static function fromStatic(string $className):
    {
    }

    /**
     * @param T $class
     *
     * @return Identifiers<T>
     */
    public static function fromInstance(string $clazz):
    {
    }

    /**
     * @return T
     */
    public function getF()
    {

    }
}

<type value="mixed|\F\Offer">Identifiers::fromInstance(new Offer)->getF()</type>;
