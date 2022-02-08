<?php
/**
 * @psalm-import-type FooAlias from Phone as MyFooAlias
 * @psalm-import-type BarAlias from Phone
 */
class User {
  /**
   * @psalm-return MyFooAlias
   */
  public function f(){}

  /**
   * @psalm-return BarAlias
   */
  public function f1(){}
}

<type value="Foo">(new User())->f()</type>;
<type value="Bar">(new User())->f1()</type>;