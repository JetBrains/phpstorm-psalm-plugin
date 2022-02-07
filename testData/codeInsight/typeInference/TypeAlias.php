<?php
class A {}
class B {}
/**
 * @psalm-type A_OR_B = A|B
 * @psalm-type myInt = int
 * @psalm-type CoolType = A_OR_B|null
 * @param myInt $a
 * @return CoolType
 */
function foo($a) {
  <type value="int">$a</type>
}


<type value="null|A|B">foo()</type>