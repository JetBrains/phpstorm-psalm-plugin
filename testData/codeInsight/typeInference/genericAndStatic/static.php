<?php

/**
 * @template T of BaseModel
 */
class Criteria
{
  /**
   * @var class-string<T>|null
   */
  protected $modelName;

  /**
   * @param class-string<T>|null $modelName
   */
  public function __construct(string $modelName = null)
  {
    $this->modelName = $modelName;
  }

  /**
   * @return T
   */
  public function one()
  {
    return new $this->modelName;
  }
}

class BaseModel
{
  final public function __construct() {}

  /**
   * @return Criteria<static>
   */
  public static function query()
  {
    return new Criteria(static::class);
  }
}

class Foo extends BaseModel
{
  public function getBar(): string
  {
    return "bar";
  }
}

class Goo extends Foo
{
  public function getBar(): string
  {
    return "bar";
  }
}

<type value="BaseModel|Foo">Foo::query()->one()</type>;
<type value="BaseModel|Goo">Goo::query()->one()</type>;
