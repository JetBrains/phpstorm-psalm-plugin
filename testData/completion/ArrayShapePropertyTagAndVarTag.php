<?php

/**
 * @property array{name: string} $magicTest
 */
class MyClass {

    /**
     * @var array{name: string, surname: string}
     */
    private $magicTest;

    private function method()
    {
        $this->magicTest['<caret>'] = "";
    }
}