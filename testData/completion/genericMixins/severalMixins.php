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
 * @template T2
 * @mixin T
 * @mixin T2
 */
class GenericFoo {}

/**
 * @param GenericFoo<MixinClass1, MixinClass2> $foo
 */
function doFoo(GenericFoo $foo): void
{
  $foo-><caret>;
}
