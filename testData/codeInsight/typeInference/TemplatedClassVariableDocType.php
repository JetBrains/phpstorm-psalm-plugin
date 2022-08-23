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
<type value="User">$usersCollection->get()</type>;