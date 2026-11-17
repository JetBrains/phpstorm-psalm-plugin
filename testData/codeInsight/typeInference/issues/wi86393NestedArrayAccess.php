<?php

class Foo {}
class Bar {}

/** @return Closure(): (Closure(Foo): array<string, Bar>) */
function nestedArrayFactory(): Closure {}

$inner = nestedArrayFactory()();
$inner(new Foo())['key'];
