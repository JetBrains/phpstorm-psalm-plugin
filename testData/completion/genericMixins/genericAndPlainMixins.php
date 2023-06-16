<?php

class MixinClass1
{
  public string $mixinClassProperty1;

  public function mixinClassMethod1() {}
}

class MixinClass2
{
  public string $mixinClassProperty2;

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
  $foo-><caret>;
}
