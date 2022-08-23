<?php
/**
 * @template T
 */
class A {
    /**
     * @return T
     */
    function get() {}
}

/**
 * @return A<User>
 */
function f() {}
<type value="User">f()->get()</type>;

/**
 * @template T
 * @template-extends A<T>
 */
class B extends A {

}

/**
 * @return B<User>
 */
function f1() {}
<type value="User">f1()->get()</type>;