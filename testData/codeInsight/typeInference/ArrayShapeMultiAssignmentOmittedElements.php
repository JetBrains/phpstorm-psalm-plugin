<?php

class type1 {}
class type2 {}
class type3 {}
/** @return array{0: type1, 1: type2, 2: type3} */
function returnArrayShape(): array
{
    return [new type1(), new type2(), new type3()];
}

[
<type value="type1">$c1</type>
,
<type value="type2">$c2</type>
] = returnArrayShape();

[,
<type value="type2">$c3</type>
] = returnArrayShape();
list(,
<type value="type2">$c3</type>
) = returnArrayShape();
list(,,
<type value="type3">$c3</type>
) = returnArrayShape();

/** @return array{0: type1, 1: type2, 2: type3} */
function declaredShapeWins(): array {
    return [new type3()];
}

[, <type value="type2">$declaredSecond</type>, <type value="type3">$declaredThird</type>] = declaredShapeWins();
<type value="type2">$declaredSecond</type>;
<type value="type3">$declaredThird</type>;
