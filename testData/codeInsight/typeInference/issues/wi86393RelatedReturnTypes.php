<?php

class Foo {}
class Bar {}

/** @return callable(): array<string, Bar> */
function callableArrayFactory(): callable {}

$callableArray = callableArrayFactory();
<type value="Bar">$callableArray()['key']</type>;

/** @return Closure(): list<Bar> */
function listFactory(): Closure {}

<type value="Bar">listFactory()()[0]</type>;

/** @return Closure(): (Closure(Foo): array<string, Bar>) */
function nestedArrayFactory(): Closure {}

$inner = nestedArrayFactory()();
<type value="Bar[]">$inner(new Foo())</type>;

/** @return Closure(): (Closure(Foo): Bar) */
function nestedScalarFactory(): Closure {}

<type value="Bar">nestedScalarFactory()()(new Foo())</type>;

/** @return callable(): (callable(Foo): Bar) */
function nestedCallableFactory(): callable {}

<type value="Bar">nestedCallableFactory()()(new Foo())</type>;

/** @return Closure(): array<string, list<Bar>> */
function nestedGenericFactory(): Closure {}

<type value="Bar">nestedGenericFactory()()['key'][0]</type>;

/** @return Closure(): (array<string, Bar>|list<Bar>) */
function unionFactory(): Closure {}

foreach (unionFactory()() as $value) {
    <type value="Bar">$value</type>;
}
