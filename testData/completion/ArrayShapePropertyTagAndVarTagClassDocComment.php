<?php

/**
 * @property array{name: string} $a
 * @var array{surname: string} $b
 */
class A {
    function method() {
        $this->a['<caret>'];
    }
}