<?php

class MixinClass
{
  public function mixinClassMethod() {}
}

/**
 * @template T
 * @mixin T
 */
class GenericFoo {}

/**
 * @param GenericFoo<MixinClass> $foo
 */
function doFoo(GenericFoo $foo): void
{
  $foo->mixinClassMethod<caret>();
}
