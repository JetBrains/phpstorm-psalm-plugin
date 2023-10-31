<?php

/**
 * @psalm-param $json object{data: object{key: array{abc : int}}
 */
function foo($json) {
    $c = $json->data->key['ab<caret>']
}