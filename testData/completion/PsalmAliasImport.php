<?php

class Foo {}
class Bar {}
/**
 * @psalm-type FooAlias = Foo
 * @psalm-type MyBarAlias = Bar
 */
class Phone {

}

/**
 * @psalm-import-type FooAlias from Phone as MyFooAlias
 * @psalm-import-type MyBarAlias from Phone
 */
class User {
  /**
   * @psalm-return My<caret>
   */
  public function f(){}
}