<?php

class a {}

/**
 * @template-covariant TKey as array-key
 * @template TValue
 * @extends Iterator<TKey, TValue>
 */
abstract class arrCovariant implements Iterator {}

/**
 * @return arrCovariant<string, a>
 */
function findAllCovariant(): arrCovariant {
}

$source = findAllCovariant();
foreach ($source as $k => $v) {
  <type value="a|mixed">$v</type>;
}
