<?php
interface I {

}

class A implements I {

	public function showA(): void {

	}
}

class B implements I {

	public function showB(): void {

	}
}

/**
 * @template T of I
 */
class Foo {

	/**
	 * @var T
	 */
	private mixed $i;

	/**
	 * @param T $i
	 */
	public function __construct(mixed $i = null) {
		$this->i = $i;
	}

	/**
	 * @return T
	 */
	public function get(): mixed {
		return $this->i;
	}
}

/**
 * @extends Foo<B>
 */
class Bar extends Foo {
	public function __construct() {
		parent::__construct(new B());
	}
}

class Baz extends Bar {

}

$bar = new Bar();
<type value="I|B|mixed">$bar->get()</type>;

$baz = new Baz();
<type value="I|B|mixed">$baz->get()</type>;
