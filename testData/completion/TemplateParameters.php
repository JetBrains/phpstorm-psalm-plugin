<?php

class Base {}

/**
 * @template TPClassSimple
 * @template TPClassWithSuper of Base
 * @template-covariant TPCovariantClass
 * @template-covariant TPCovariantClassWithSuper of Base
 * @template-contravariant TPContravariantClassWithSuper of Base
 */
class Foo {
  /**
   * @template TPFunctionSimple
   * @template TPFunctionWithSuper of Base
   * @template-covariant TPCovariantFunction
   * @template-covariant TPCovariantFunctionWithSuper of Base
   * @template-contravariant TPContravariantFunctionWithSuper of Base
   *
   * @return TP<caret>
   */
  function method() {

  }
}
