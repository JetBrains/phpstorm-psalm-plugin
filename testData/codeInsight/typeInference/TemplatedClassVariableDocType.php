<?php

/**
 * @template T
 */
class Collection
{
    /** @return T */
    public function get() {}
}





/** @var Collection<User> $usersCollection */
$usersCollection = new Collection();
<type value="mixed|User">$usersCollection->get()</type>;