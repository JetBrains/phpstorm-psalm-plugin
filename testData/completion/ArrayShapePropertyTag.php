<?php

/**
 * @property array{name: string} $magicTest
 */
class MyClass {

    private function method()
    {
        $this->magicTest['<caret>'] = "";
    }
}