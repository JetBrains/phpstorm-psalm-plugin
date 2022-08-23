<?php
/**
 * @template T
 */
class C1{
    /**
     * @return C2<T>
     */
    public function f(){}
}
/**
 * @template T
 */
class C2{
    /**
     * @return T
     */
    public function f(){}

}
/** @var $a C1<Exception> */
<type value="Exception">$a->f()->f()</type>;
