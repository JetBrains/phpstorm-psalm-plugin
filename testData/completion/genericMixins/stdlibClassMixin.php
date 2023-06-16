<?php

declare(strict_types=1);

/**
 * @template T
 * @mixin T
 */
class GenericFoo {
}

class Test {
  /**
   * @param GenericFoo<\ReflectionClass> $foo
   */
  public function doFoo($foo): void {
    var_dump($foo-><caret>);
  }
}
