<?php
namespace x;
/**
 * @psalm-param int $a
 */
function f(\string $a)
{
<type value="int|string">$a</type>;
}