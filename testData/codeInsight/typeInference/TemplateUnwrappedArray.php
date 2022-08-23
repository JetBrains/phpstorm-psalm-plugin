<?php

/**
 * @template Tk
 * @template Tv
 * @param array<Tk, Tv> $containers
 * @return Tv
 */
function unwrap($containers)
{
}

<type value="string">unwrap(["a"])</type>;

/**
 * @template Tv
 * @param list<Tv> $containers
 * @return Tv
 */
function unwrapList($containers)
{
}

<type value="int">unwrapList([1])</type>;

/**
 * @template Tv
 * @template Tk
 * @param array<Tk, Tv> $containers
 * @return Tk
 */
function unwrapListWrong($containers)
{
}
<type value="">unwrapListWrong(["a"])</type>;