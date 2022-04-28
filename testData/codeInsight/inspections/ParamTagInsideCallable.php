<?php
class QQ {
    /**
     * @template V
     * @param callable(string):V $operation
     * <weak_warning descr="Doc tag without variable name doesn't provide type information for any expression">@param callable(string):V</weak_warning>
     */
    public function foo(callable $operation)
    {
        return $operation("Hello World");
    }
}
