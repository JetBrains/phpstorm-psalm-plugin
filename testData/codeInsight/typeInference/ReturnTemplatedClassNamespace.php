<?php
namespace N;
/**
 * @template R
 * @template D
 */
interface I
{
}

class A {}
class B {}

namespace N1;

use N\A;
use N\B;
use N\I;
/**
 * @template-implements I<A, B>
 */
class C1 implements I {}

/**
 * @template-implements I<B, A>
 */
class C2 implements I {}

/**
 * @template R
 * @psalm-param I<R> $q
 * @return R
 */
function f(I $q) {}
/**
 * @template R
 * @template D
 * @psalm-param I<R, D> $q
 * @return R
 */
function f1(I $q) {}
/**
 * @template R
 * @template D
 * @psalm-param I<R, D> $q
 * @return D
 */
function f2(I $q) {}

/**
 * @template R
 * @template D
 * @psalm-param I<I<R>, I<D>> $q
 * @return I<R>
 */
function f3(I $q) {}

/**
 * @template R
 * @template D
 * @psalm-param I<I<R>, D> $q
 * @return I<I<R>>
 */
function f4(I $q) {}

class AA {
    /**
     * @template R
     * @template D
     * @psalm-param I<R, D> $q
     * @return R
     */
    function m(I $q) {}
}

<type value="\N\A">f(new C1())</type>;
<type value="\N\B">f(new C2())</type>;

<type value="\N\A">f1(new C1())</type>;
<type value="\N\B">f1(new C2())</type>;

<type value="\N\B">f2(new C1())</type>;
<type value="\N\A">f2(new C2())</type>;

<type value="\N\I">f3(new C1())</type>;
<type value="\N\I">f3(new C2())</type>;

// unknown yet
<type value="\N\I">f4(new C1())</type>;
<type value="\N\I">f4(new C2())</type>;

<type value="\N\A">(new AA())->m(new C1())</type>;
<type value="\N\B">(new AA())->m(new C2())</type>;