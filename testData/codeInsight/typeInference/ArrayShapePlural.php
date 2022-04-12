<?php
/**
 * @return array{one: string, two: string[]}
 */
function ff(){}

<type value="mixed|string">ff()['one']</type>;
<type value="mixed|string[]">ff()['two']</type>;
<type value="mixed|string">ff()['two'][0]</type>;