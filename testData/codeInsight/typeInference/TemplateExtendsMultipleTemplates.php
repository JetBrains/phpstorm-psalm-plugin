<?php

/**
 * @template T
 * @template T1
 */
abstract class Base
{


    /**
     * @var T
     */
    public $first;

    /**
     * @var T1
     */
    public $second;
}

class P
{
}
class P1
{
}


/**
 * @extends Base<P, P1>
 */
class Child extends Base
{
}

/**
 * @extends Base<P>
 */
class ChildPartial extends Base
{
}

<type value="mixed|P">(new Child())->first</type>;
<type value="mixed|P1">(new Child())->second</type>;
<type value="mixed|P">(new ChildPartial())->first</type>;
<type value="mixed">(new ChildPartial())->second</type>;