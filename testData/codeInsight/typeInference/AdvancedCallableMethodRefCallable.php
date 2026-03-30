<?php

/**
 * @template A
 */
class Option {
    /**
     * @template B
     *
     * @param callable(A): B $ab
     * @return Option<B>
     */
    public function map(callable $ab): Option {}

    /**
     * @return A
     */
    public function f() {

    }
}

/** @var Option<int> $numOption */
$numOption = new Option();
<type value="string">$mapped</type> = $numOption->map(fn($i) => (string) $i)->f();

/** @var callable(int):string $f */
$f = fn($i) => (string) $i;
<type value="string">$mappedByVariable</type> = $numOption->map($f)->f();
