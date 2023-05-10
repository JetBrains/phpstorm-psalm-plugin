<?php

class Foo {

  /**
   * @return ArrayObject<static>
   */
  public static function factory() {
    $object = new static();
    return new ArrayObject([$object]);
  }

}

class Bar extends Foo {
  public function baz() {
    return 'Hello World!';
  }
}

$bars = Bar::factory();
foreach ($bars as $bar) {
  <type value="mixed|Bar">$bar</type>;
}
