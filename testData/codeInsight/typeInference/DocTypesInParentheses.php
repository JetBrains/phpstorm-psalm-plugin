<?php

/**
 * @param (A&B)[]|null $foo
 * @param (A|B)[]|null $foo1
 * @param (callable(): int|float)|string $foo2
 */
function mockBar($foo, $foo1, $foo2)
{
    <type value="null|\B&\A[]">$foo</type>;
    <type value="null|B[]|A[]">$foo1</type>;
    <type value="string|float|callable">$foo2</type>;
    <type value="int">$foo2()</type>;
}