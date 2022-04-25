<?php


/**
 * @template T
 */
class C {

    /**
     * @template T1
     * @param C<T1> $a
     * @return C<array<T1>>
     */
    public function create($a)
    {

    }

    /**
     * @return T
     */
    public function aa()
    {

    }
}

/**
 * @param C<B> $a
 * @param C<B1> $ab
 * @return void
 */
function ff($a, C $ab) {
    <type value="B1|mixed">$a->create($ab)->aa()[0]</type>
}
