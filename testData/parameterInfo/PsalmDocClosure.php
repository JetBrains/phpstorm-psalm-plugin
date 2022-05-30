<?php
/**
 * @return Closure(bool, int $j): int
 */
function createCallable(): Closure
{}
$callable = createCallable();

$callable(<caret>);