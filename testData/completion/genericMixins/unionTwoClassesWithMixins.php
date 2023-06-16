<?php
declare(strict_types=1);

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
 */
class GenericFoo {}

/**
 * @template T
 * @mixin T
 */
class GenericFoo2 {}

/**
 * @param GenericFoo2<MixinClass2> | GenericFoo<MixinClass1> $foo
 */
function doFoo(GenericFoo2|GenericFoo $foo): void
{
  $foo-><caret>;
}
