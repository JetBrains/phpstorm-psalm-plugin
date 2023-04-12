<?php

/**
 * @template T
 */
class Collection {
  /** @var array<array-key, T> */
  protected array $stack = [];

  /**
   * @param array-key $arrayKey
   * @return T
   */
  public function fetch(string|int $arrayKey): mixed {
    return $this->stack[$arrayKey];
  }
}

class Cat {
  public function __construct(
    public string $breed,
  ) {
  }
}

class Test {
  /** @var Collection<Cat> */
  public Collection $collectionA;

  /**
   * @param Collection<Cat> $collectionC
   */
  public function __construct(
    /** @var Collection<Cat> */
    public Collection $collectionB,
    public Collection $collectionC,
  ) {
  }
}

$test = new Test(null, null);
// Classic property using @var
<type value="mixed|Cat">$test->collectionA->fetch(0)</type>;
// Promoted property using @var
<type value="mixed|Cat">$test->collectionB->fetch(0)</type>;
// Promoted property using method @param
<type value="mixed|Cat">$test->collectionC->fetch(0)</type>;
