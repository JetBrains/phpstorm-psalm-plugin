<?php

/** @psalm-param array{int, data: array<object{key: array{some: string}}>} $json */
function foo($json) {
    $cert = $json['data'][0]->k<caret>
}
