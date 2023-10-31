<?php
/** @psalm-param array{data: array{key: object{abc : int}} $json */
function foo($json) {
    $c = $json['data']['key']->a<caret>
}
