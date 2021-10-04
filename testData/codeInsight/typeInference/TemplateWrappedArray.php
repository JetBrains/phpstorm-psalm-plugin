<?php

/**
 *
 * @template T
 *
 * @param T $value
 *
 * @return array<T>
 */
function fill(mixed $value): array
{
}

/**
 *
 * @template T
 *
 * @param T $value
 *
 * @return array<int, T>
 */
function fillWithKeys(mixed $value): array
{
}


/**
 *
 * @template T
 *
 * @param int $a
 * @param T $value
 *
 * @return list<T>
 */
function fillCustomParameter($a, mixed $value): array
{
}

/**
 *
 * @template T
 *
 * @param int $a
 * @param T $value
 *
 * @return list<int>
 */
function fillNoGenerics($a, mixed $value): array
{
}

<type value="array|A[]">fill(new A)</type>;
<type value="array|A[]">fillWithKeys(new A)</type>;
<type value="array|A[]">fillCustomParameter(new B, new A)</type>;
<type value="int[]">fillNoGenerics(new B, new A)</type>;