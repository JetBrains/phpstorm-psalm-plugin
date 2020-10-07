<?php
/**
 * @template MyTemp1
 * @psalm-type MyTemp2 = array{name: string, age: int}
 */
class MyContainer {
    /**
     * @psalm-import-type NameType from Name as MyTemp3
     * @var MyTemp<caret>
     */
    private $value;
}