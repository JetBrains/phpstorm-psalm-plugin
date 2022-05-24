<?php

/**
 * @return array{prop1: string, prop2: string}|array{prop3: string, prop4: string}
 */
function test() { };

test()['<caret>'];
