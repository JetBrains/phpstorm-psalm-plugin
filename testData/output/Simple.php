<weak_warning descr="psalm: TypeDoesNotContainType: Found a contradiction when evaluating $condition and trying to reconcile type 'int(0)' to !falsy"></weak_warning><?php

/**
<weak_warning descr="psalm: InvalidReturnType: The declared return type 'array<array-key, string>' for takesAnInt is incorrect, got 'array{0: int, 1: string(hello)}'"> * @return array<string></weak_warning>
 */
function takesAnInt(int $i) {
    return [$i, "hello"];
<weak_warning descr="psalm: InvalidReturnStatement: The inferred type 'array{0: int, 1: string(hello)}' does not match the declared return type 'array<array-key, string>' for takesAnInt">}</weak_warning>

$data = ["some text", 5];
takesAnInt($data[0]);
<weak_warning descr="psalm: InvalidScalarArgument: Argument 1 of takesAnInt expects int, string(some text) provided"></weak_warning>
$condition = rand(0, 5);
if ($condition) {
} elseif ($condition) {}