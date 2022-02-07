<?php

/**
 * @method array<Foo> f()
 * @method Foo[] ff()
 * @method Foo fff()
 */
class FooQuery {

}

<type value="array|Foo[]">(new FooQuery())->f()</type>;
<type value="Foo[]">(new FooQuery())->ff()</type>;
<type value="Foo">(new FooQuery())->fff()</type>;
