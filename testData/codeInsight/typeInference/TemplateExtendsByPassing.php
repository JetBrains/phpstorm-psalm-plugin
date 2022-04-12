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

}

/**
 * @return ArrayCollection<int, \DateTime>
 */
function getDates(): ArrayCollection
{
    return new ArrayCollection([new \DateTime()]);
}

foreach (getDates() as $date) {
    <type value="DateTime">$date</type>
}
