<?php

class MixinClass
{
  public string $mixinClassProperty;

  public function mixinClassMethod() {}
}

/**
 * @template T
 * @mixin T
 */
class GenericFoo
{
  public function genericFooMethod() {}
}

/**
 * @param GenericFoo<MixinClass> $foo
 */
function doFoo(GenericFoo $foo): void
{
  $foo-><caret>;
}
