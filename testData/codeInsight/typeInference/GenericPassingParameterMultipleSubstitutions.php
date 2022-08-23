<?php
/**
 * @template T
 * @template T1
 */
class C1{
    /**
     * @return C2<T, T1>
     */
    public function f(){}
}
/**
 * @template T
 * @template T1
 */
class C2{
    /**
     * @return T|T1
     */
    public function f(){}

}
/** @var $a C1<A, B> */
<type value="A|B">$a->f()->f()</type>;
