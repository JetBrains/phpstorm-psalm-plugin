<?php
namespace A;
/**
 * @template T
 */
abstract class Base
{
    /**
     * @return T
     */
    public function item()
    {
    }

    /**
     * @return array<T>
     */
    public function items()
    {
    }

    /**
     * @var T
     */
    public $item;
    /**
     * @var array<T>
     */
    public $items;
}

namespace B;

use A\Base;

class P
{
}
/**
 * @extends Base<P>
 */
class Child extends Base
{
}

$child = new Child();
<type value="\B\P">$child->item</type>;
<type value="array|\B\P[]">$child->items</type>;
<type value="\B\P">$child->item()</type>;
<type value="array|\B\P[]">$child->items()</type>;

class P1
{
}

/**
 * @template-extends Base<P1>
 */
class Child1 extends Base
{
}

$child1 = new Child1();
<type value="\B\P1">$child1->item</type>;
<type value="array|\B\P1[]">$child1->items</type>;
<type value="\B\P1">$child1->item()</type>;
<type value="array|\B\P1[]">$child1->items()</type>;