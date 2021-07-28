<?php
namespace N;
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
<type value="\N\User|mixed">f()->get()</type>;
namespace N1;
use N\A;
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
<type value="mixed|\N1\User|T">f1()->get()</type>;