<?php
namespace f;
/** @psalm-var Closure(bool):int $f */
$f = ff();
<type value="Closure">$f</type>

/** @psalm-var Closure $f1 */
$f1 = ff1();
<type value="\f\Closure">$f1</type>
