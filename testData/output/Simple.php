<?php
<warning descr="psalm: UnusedVariable: Variable $fff is never referenced">$fff = "";</warning>
/**
 * @return <warning descr="psalm: InvalidReturnType: The declared return type 'array<array-key, string>' for takesAnInt is incorrect, got 'array{int, string(ho)}'">array<string></warning>
 * halifax templated
 */
function takesAnInt(int $i) {
    return <warning descr="psalm: InvalidReturnStatement: The inferred type 'array{int, string(ho)}' does not match the declared return type 'array<array-key, string>' for takesAnInt">[$i, "ho"];</warning>
}

$data = ["some text", 5];
takesAnInt(<warning descr="psalm: InvalidScalarArgument: Argument 1 of takesAnInt expects int, string(some text) provided">$data[0]);</warning>
/**
 * @psalm-suppress <warning descr="psalm: UnusedPsalmSuppress: This suppression is never used">InvalidReturnType</warning>
 */
$condition = rand(0, 5);
if ($condition) {
<warning descr="psalm: TypeDoesNotContainType: Found a contradiction when evaluating $condition and trying to reconcile type 'int(0)' to !falsy">} elseif ($condition) {}</warning>