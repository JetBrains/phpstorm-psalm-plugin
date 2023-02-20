<?php

/** @var 'one'|'two' $param */
/** @var "one"|"two" $param */
/** @var "two"|<weak_warning descr="Duplicate type '\"two\"'">"two"</weak_warning> $param */
/** @var 'one'|<weak_warning descr="Duplicate type ''one''">'one'</weak_warning> $param */
/** @var ''|<weak_warning descr="Duplicate type ''''">''</weak_warning> $param */
/** @var ' '|'' $param */
function foo($param) {}
