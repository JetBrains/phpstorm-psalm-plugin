<?php

class Foo {
  static function staticMethod() {}
  static function staticMethod2() {}
  function nonStaticMethod2() {}
}

/** @var array<class-string<Foo>> $arr */
$arr = [];

foreach ($arr as $key => $value) {
  $value::<caret>;
}
