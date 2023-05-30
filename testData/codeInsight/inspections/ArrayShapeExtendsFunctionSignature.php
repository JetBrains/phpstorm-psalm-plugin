<?php

/**
 * @return array{Exception,int}
 */
function f1(Exception $type): array
{
}

/**
 * @return array{Exception,int}
 */
function f2(): int
{
}

/**
 * @return array{Exception,int}|int
 */
function f3()
{
}

/**
 * @param $a array{Exception,int}
 */
function f4(array $a, $b): int
{
}

/**
 * @return array{Exception,int}
 */
function f5(): array
{
}