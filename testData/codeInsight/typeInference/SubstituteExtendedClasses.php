<?php

/**
 * @template T
 * @template T1
 */
interface TemplatedInterface
{
    /** @return T */
    public function getFirst();

    /** @return T1 */
    public function getSecond(): mixed;
}

/**
 * @template T
 * @template T1
 * @template-implements TemplatedInterface<T1, T>
 */
class TemplatedClass implements TemplatedInterface
{
    /**
     * @param T $value
     * @param T1 $value1
     */
    public function __construct(mixed $value, $value1)
    {
    }

    public function getFirst()
    {
    }

    public function getSecond()
    {
    }
}
$example = new TemplatedClass(new A, new B);
<type value="null|B">$example->getFirst()</type>;
<type value="null|A">$example->getSecond()</type>;