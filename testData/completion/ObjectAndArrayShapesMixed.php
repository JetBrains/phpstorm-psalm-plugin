<?php
/**
 * @psalm-param array{data: array{key: object{abc : object{nnn: array{def: object{lll: object{amb: int}}}}} $json
 */
function foo($json) {
    $c = $json['data']['key']->abc->nnn['def']->lll->a<caret>
}