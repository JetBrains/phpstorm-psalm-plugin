<?php

class MixinClass
{
  public string $mixinClassProperty;
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
  $foo-><caret>mixinClassProperty;
}
