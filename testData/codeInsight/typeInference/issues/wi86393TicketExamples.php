<?php

class Foo {}
class Bar {}

/** @return Closure(): array<string, Bar> */
function arrayFactory(): Closure {}

$arrayClosure = arrayFactory();
$items = $arrayClosure();
$items['key']; // Expected Bar; actual type is unknown.
foreach ($items as $item) {
    $item; // Expected Bar; actual type is unknown.
}

/** @return Closure(): list<Bar> */
function listFactory(): Closure {}

$listClosure = listFactory();
foreach ($listClosure() as $item) {
    $item; // Expected Bar; actual type is unknown.
}

/** @return Closure(): (Closure(Foo): Bar) */
function nestedFactory(): Closure {}

$outer = nestedFactory();
$inner = $outer();
$inner(new Foo()); // Expected Bar; actual type is unknown.
