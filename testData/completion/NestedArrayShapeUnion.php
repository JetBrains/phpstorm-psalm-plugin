<?php

function f(){
    /** @var array{person: array{name: string}|array{surname: string}} $test */
    $test['person']['<caret>'];
}
