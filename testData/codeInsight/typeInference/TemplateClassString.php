<?php

class A
{
}

class B
{
}

/**
 * @template T
 * @template F
 * @param class-string<T> $a
 * @param class-string<F> $a1
 * @return T
 */
function get(string $a, string $a1) {}

<type value="A|mixed">get("A", "B")</type>;


/**
 * @template T
 * @template F
 * @param class-string<T> $a
 * @param class-string<F> $a1
 * @return T&F
 */
function get1(string $a, string $a1) {}

<type value="null|A|B">get1("A", "B")</type>;


/**
 * @template T
 * @template F
 * @param class-string<T> $a
 * @param class-string<F> $a1
 * @return F
 */
function get2(string $a, string $a1) {}

<type value="B|mixed">get2( "A", "B")</type>;


/**
 * @template T
 * @template F
 * @param class-string<T> $a
 * @param class-string<F> $a1
 */
function get3(string $a, string $a1) {}

<type value="null">get3( "A", "B")</type>;

