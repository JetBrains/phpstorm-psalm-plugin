<?php

class type1 {}
class type2 {}
class type3 {}
/** @return array{0: type1, 1: type2, 2: type3} */
function returnArrayShape(): array
{
    return [new type1(), new type2(), new type2()];
}

[
<type value="mixed|type1">$c1</type>
,
<type value="mixed|type2">$c2</type>
] = returnArrayShape();

[,
<type value="mixed|type2">$c3</type>
] = returnArrayShape();
list(,
<type value="mixed|type2">$c3</type>
) = returnArrayShape();
list(, list(),
<type value="mixed|type3">$c3</type>
) = returnArrayShape();