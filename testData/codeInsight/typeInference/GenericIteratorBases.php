<?php

interface I0 {
    public function f0(): void;
}

interface I1 {
    public function f1(): void;
}

/**
 * @param iterable<I0, I1> $iterable
 * @param \Iterator<I0, I1> $iterator
 * @param \IteratorAggregate<I0, I1> $ia
 * @param \Traversable<I0, I1> $traversable
 * @param \Iterator|I1[] $itPseudo
 */
function foo(iterable $iterable, \Iterator $iterator, \IteratorAggregate $ia, \Traversable $traversable, \Iterator $itPseudo) {
    foreach ($iterable as $k => $v) {
        <type value="I0">$k</type>;
        <type value="I1|mixed">$v</type>;
    }
    foreach ($iterator as $k => $v) {
        <type value="I0">$k</type>;
        <type value="I1|mixed">$v</type>;
    }
    foreach ($ia as $k => $v) {
        <type value="I0">$k</type>;
        <type value="I1">$v</type>;
    }
    foreach ($traversable as $k => $v) {
        <type value="I0">$k</type>;
        <type value="I1">$v</type>;
    }
    foreach ($itPseudo as $k => $v) {
        <type value="null[]|array">$k</type>; // expected
        <type value="I1|mixed">$v</type>;
    }
}
