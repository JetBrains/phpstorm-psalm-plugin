<?php

class type1 {}
class type2 {}
class type3 {}
/** @return array{0: type1, 1: type2, 2: type3} */
function returnArrayShape(): array
{
    return [new type1(), new type2(), new type3()];
}

// An empty nested list is invalid PHP. Check inference after parser recovery.
list(, list(),
<type value="type3">$c3</type>
) = returnArrayShape();
