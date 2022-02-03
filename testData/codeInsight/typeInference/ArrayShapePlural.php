<?php
/**
 * @return array{one: string, two: string[]}
 */
function ff(){}

<type value="string|mixed">ff()['one']</type>;
<type value="mixed|string[]">ff()['two']</type>;
<type value="string|mixed">ff()['two'][0]</type>;