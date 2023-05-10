<?php

class BaseRecord {
  /**
   * @return QueryBuilder<static>
   */
  public static function BeginQuery(): QueryBuilder {
    return new QueryBuilder(static::class);
  }
}

class ExampleSubclassExtendingBase extends BaseRecord {
  public int $fooValue = 0;
}

/**
 * @template T extends BaseRecord
 */
class QueryBuilder {
  /**
   * @param class-string<T> $className
   */
  public function __construct(private string $className) {}

  /**
   * @return T
   */
  public function one() {
    return new $this->className();
  }
}

$c = ExampleSubclassExtendingBase::BeginQuery()->one();
<type value="mixed|ExampleSubclassExtendingBase">$c</type>;
