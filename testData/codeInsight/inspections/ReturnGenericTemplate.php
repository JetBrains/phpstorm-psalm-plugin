<?php
class QQ {
    /**
     * @template V
     * @param callable(string):V $operation
     */
    public function foo(callable $operation)
    {
        $operation("Hello World");
        $operation(<warning descr="Parameter type 'array' is not compatible with 'string'">[]</warning>);
    }
}
