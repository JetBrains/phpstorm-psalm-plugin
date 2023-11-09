<?php
/** @psalm-param array{int, object{key: array{some: string}} $json */
function foo($json) {
    $cert = $json[1]-><caret>
}
