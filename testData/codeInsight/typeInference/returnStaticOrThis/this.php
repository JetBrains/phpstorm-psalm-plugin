<?php

/** @template T */
class Foo
{
  /** @param T $item */
  public function __construct(private $item)
  {
  }

  /** @return $this */
  public function chain()
  {
    return $this;
  }

  /** @return T */
  public function item()
  {
    return $this->item;
  }
}

class Bar
{
  function value(): string {
    return "";
  }
}

$f = (new Foo(new Bar()))
  ->chain()
  ->item();

<type value="mixed|Bar">$f</type>;
