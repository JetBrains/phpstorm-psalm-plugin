<?php

class User{
    public function f(){

    }
}
class UserService
{
    public function doSomething(): void
    {
        foreach ($this->getUsers() as $user) {
            <type value="User">$user</type>;
        }
    }
    /**
     * @return MyCollection<int, User>
     */
    function getUsers(): MyCollection{}
}

/**
 * @phpstan-template TKey
 * @psalm-template TKey of array-key
 * @psalm-template T
 * @phpstan-template T
 */
class MyCollection implements \IteratorAggregate
{
    /**
     * @return T[]
     */
    public function getIterator(){
    }
}