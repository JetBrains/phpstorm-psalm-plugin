<?php

class A {
  /**
	 * @var positive-int
	 */
	private int $infiniteLength = 100500;
}

/**
 * @param class-string $class
 */
function test(string $class): bool {
    return true;
}

/**
 * @param \Closure(int, string) : ?bool $c
 */
function registerClosure(string $first, \Closure $c): void
{

}