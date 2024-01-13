<?php

namespace Test1;

class Foo {
  static function staticMethod() {}
  static function staticMethod2() {}
  function nonStaticMethod2() {}
}

namespace Test2;
use Test1\Foo;

/** @var array<class-string<Foo>> $arr */
$arr = [];

foreach ($arr as $key => $value) {
  $value::<caret>;
}
