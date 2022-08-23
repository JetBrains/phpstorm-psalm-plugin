<?php

function f(){
    /** @var array{x: array{person: array{name?: Exception, age: int}}} $test */
    $test = [];

  foreach ($test['x'] as $item) {
    $item['<caret>'];
  }
}