<?php

class Bar {}

/**
 * @template T
 */
class GenericFactory {
    /** @return Closure(): list<T> */
    public function make(): Closure {}
}

/** @var GenericFactory<Bar> $factory */
$factory = new GenericFactory();
<type value="Bar">$factory->make()()[0]</type>;
