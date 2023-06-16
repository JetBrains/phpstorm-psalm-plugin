<?php

class ApiClient {
  public function request() {}
}

/**
 * @template T
 * @mixin T
 */
class CachingDecorator {
  public function __construct(private object $object) {}
}

/** @var CachingDecorator<ApiClient> $foo */
$foo = new CachingDecorator(new ApiClient());
$foo-><caret>;
