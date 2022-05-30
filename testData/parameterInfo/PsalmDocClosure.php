<?php
/**
 * @return Closure(bool, int): int
 */
function createCallable(): Closure
{}
$callable = createCallable();

$callable(<caret>);