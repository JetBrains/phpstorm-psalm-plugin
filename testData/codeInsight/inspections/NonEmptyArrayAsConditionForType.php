<?php

class Foo {
  /**
   * @template TV
   *
   * @psalm-return (
   *     $collections is non-empty-array
   *         ? non-empty-list<TV>
   *         : list<TV>
   * )
   */
  function foo($collections) {}
}
