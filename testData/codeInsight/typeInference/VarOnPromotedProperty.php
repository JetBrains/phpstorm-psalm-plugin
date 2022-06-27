<?php
class A{}
class B{}
class ModifierNode
{
    public function __construct(
        /** @var A[] */
        public array $a,
        /** @psalm-var B[] */
        public array $b,
        public array $c,
	) {
    }
}






<type value="A">(new ModifierNode())->a[0]</type>;
<type value="B">(new ModifierNode())->b[0]</type>;
<type value="mixed">(new ModifierNode())->c[0]</type>;
