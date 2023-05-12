<?php

/**
 * @template T
 */
class Collection implements \IteratorAggregate {
  /** @param array<T> $items */
  public function __construct($items = []) { }

  /**
   * @return array<T>
   */
  public function all() { }

  /** @return $this */
  public function filter(callable $callback = null) { }

  /** @return iterable<T> */
  public function getIterator() { }
}

/** @return Collection<A> */
function getCollection() { }


class A {
  public function some(): void { }
}

$a = getCollection();
$a = $a->filter();
$a = $a->filter();

foreach ($a as $item) {
  <type value="mixed|A">$item</type>;
}
