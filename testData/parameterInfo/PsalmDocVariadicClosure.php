<?php

/** @return Closure(string ...$items): bool */
function makeClosure(): Closure {}

$closure = makeClosure();
$closure(<caret>);
