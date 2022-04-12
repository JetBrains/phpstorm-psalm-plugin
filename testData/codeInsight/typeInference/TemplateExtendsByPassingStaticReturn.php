<?php
/**
 * @template T
 * @template-extends IteratorAggregate<T>
 * @template-extends ArrayAccess<T>
 */
interface Collection extends Countable, IteratorAggregate, ArrayAccess{}

/**
 * @template T
 * @template-implements Collection<T>
 */
class ArrayCollection implements Collection{
    /**
     * @return static<T>
     */
    public function map()
    {
    }

    /**
     * @return T
     */
    function get() {}

}

/**
 * @return ArrayCollection<DateTime>
 */
function getDates(): Collection
{
    return new ArrayCollection([new \DateTime()]);
}

<type value="mixed|DateTime">getDates()->get(0)</type>;

foreach (getDates()->map() as $user) {
    <type value="T|mixed|DateTime">$user</type>;
}

<type value="mixed|DateTime">getDates()->map()->get(0)</type>;
