<?php

class A
{
  /**
   * @return C<$this>
   */
  public function query(): C
  {
    return new C($this);
  }
}

class B extends A
{
}

/**
 * @template TModel
 */
class C
{
  /**
   * @param TModel $model
   */
  public function __construct(private $model)
  {
  }

  /**
   * @return TModel
   */
  public function model()
  {
    return $this->model;
  }
}

$c = (new B())->query();
<type value="C">$c</type>;
<type value="mixed|B">$c->model()</type>;

$c2 = (new A())->query();
<type value="C">$c2</type>;
<type value="A|mixed">$c2->model()</type>;
