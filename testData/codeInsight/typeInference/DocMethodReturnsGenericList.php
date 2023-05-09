<?php

class Boo {}

/**
 * @method list<Boo> bar()
 */
class Foo {}

$foo = new Foo();
$res = $foo->bar();
<type value="array|list|Boo[]">$res</type>;
foreach ($res as $item) {
    <type value="mixed|Boo">$item</type>;
}
