<?php

/**
 * @template T
 */
class Collection {
  /**
   * @var array<T>
   */
  private array $items;

  public function __construct(array $items) {
    $this->items = $items;
  }

  /**
   * @return array<T>
   */
  public function all(): array {
    return $this->items;
  }


}

class BaseQuery {
  /**
   * @return Collection
   */
  public function all(): Collection {
    return new Collection([]);
  }
}

class Foo {

}

/**
 * @method Collection<Foo> getCollection()
 */
class FooQuery extends BaseQuery {

}

$collection = (new FooQuery())->getCollection()->all();

foreach ($collection as $item) {
  <type value="Foo|mixed">$item</type>;
}
