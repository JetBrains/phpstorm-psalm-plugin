<?php

/** @psalm-param array{data: array<array{key: string}>} $json */
function foo($json) {
    $cert = $json['data'][0]['k<caret>'];
}
