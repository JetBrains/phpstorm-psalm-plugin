<?php

/**
 * @template T
 * @method list<T> getItems()
 */
class Collection {}

/** @var Collection<stdClass> $collection */
$collection = new Collection();
$items = $collection->getItems();
<type value="array|list|stdClass[]">$items</type>;

foreach ($items as $item) {
    <type value="mixed|stdClass">$item</type>;
}
