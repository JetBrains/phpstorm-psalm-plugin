<?php
class B {}
/** @psalm-type MyB = B */
class PhoneType {}
/**
 * @psalm-import-type  MyB from PhoneType
 */
class CC
{
    /**
     * @return array<MyB>
     */
    public function f1(){}

    /**
     * @return MyB[]
     */
    public function f2(){}

    /**
     * @return MyB[][]
     */
    public function f3(){}
}


<type value="B[]">(new CC())->f1()</type>;
<type value="B[]">(new CC())->f2()</type>;
<type value="B[][]">(new CC())->f3()</type>;