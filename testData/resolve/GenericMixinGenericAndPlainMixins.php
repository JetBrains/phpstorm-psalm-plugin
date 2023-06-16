<?php

class MixinClass1
{
  public function mixinClassMethod1() {}
}

class MixinClass2
{
  public function mixinClassMethod2() {}
}

/**
 * @template T
 * @mixin T
 * @mixin MixinClass2
 */
class GenericFoo {}

/**
 * @param GenericFoo<MixinClass1> $foo
 */
function doFoo(GenericFoo $foo): void
{
  $foo->mixinClassMethod2<caret>();
}
