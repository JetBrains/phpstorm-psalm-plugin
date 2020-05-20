<?php
$<weak_warning descr="psalm: UnusedVariable: Variable $fff is never referenced">fff = "";</weak_warning>
/**
 * @return a<weak_warning descr="psalm: InvalidReturnType: The declared return type 'array<array-key, string>' for takesAnInt is incorrect, got 'array{int, string(ho)}'">rray<string></weak_warning>
 * halifax templated
 */
function takesAnInt(int $i) {
    return [<weak_warning descr="psalm: InvalidReturnStatement: The inferred type 'array{int, string(ho)}' does not match the declared return type 'array<array-key, string>' for takesAnInt">$i, "ho"];</weak_warning>
}

$data = ["some text", 5];
takesAnInt($<weak_warning descr="psalm: InvalidScalarArgument: Argument 1 of takesAnInt expects int, string(some text) provided">data[0]);</weak_warning>
/**
 * @psalm-suppress I<weak_warning descr="psalm: UnusedPsalmSuppress: This suppression is never used">nvalidReturnType</weak_warning>
 */
$condition = rand(0, 5);
if ($condition) {
} elseif ($<weak_warning descr="psalm: TypeDoesNotContainType: Found a contradiction when evaluating $condition and trying to reconcile type 'int(0)' to !falsy">condition) {}</weak_warning>