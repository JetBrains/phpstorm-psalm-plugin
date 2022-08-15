<?php

function f(){
    /** @var array{person: array{name?: Exception, age: int}} $test */
    $test = [];

  foreach ($test as $item) {
    $item['<caret>'];
  }
}