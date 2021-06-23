<?php

/**
 * @template R
 * @template D
 */
interface I
{
}

class A {}
class B {}
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
 * @template D
 * @psalm-param I<I<R>, I<D>> $q
 * @return I<R>
 */
function f3(I $q) {}



<type value="A|I">f3(new C1())</type>;
<type value="I|B">f3(new C2())</type>;