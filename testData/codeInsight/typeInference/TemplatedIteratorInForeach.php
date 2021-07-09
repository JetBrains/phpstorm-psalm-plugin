<?php

class User{
    public function f(){

    }
}
class UserService
{
    public function doSomething(): void
    {
        $users = $this->getUsers();
        foreach ($users as $user) {
            <type value="User">$user</type>;
        }

        $users1 = $this->getUsers1();
        foreach ($users1 as $user1) {
            <type value="int">$user1</type>;
        }

        $users2 = $this->getUsers2();
        foreach ($users2 as $user2) {
            <type value="User">$user2</type>;
        }

        $users3 = $this->getUsers3();
        foreach ($users3 as $user3) {
            <type value="">$user3</type>;
        }
    }
    /**
     * @return MyCollection<int, User>
     */
    function getUsers(): MyCollection{}

    /**
     * @return MyCollection<User, int>
     */
    function getUsers1(): MyCollection{}

    /**
     * @return MyAnotherCollection<User, int>
     */
    function getUsers2(): MyAnotherCollection{}

    /**
     * @return MyCollectionWithoutTemplate<User, int>
     */
    function getUsers3(): MyCollectionWithoutTemplate{}
}

/**
 * @template TKey of array-key
 * @template T
 */
class MyCollectionWithoutTemplate implements \IteratorAggregate
{
    public function getIterator(){
    }
}

/**
 * @template TKey of array-key
 * @template T
 */
class MyAnotherCollection implements \IteratorAggregate
{
    /**
     * @return TKey[]
     */
    public function getIterator(){
    }
}
/**
 * @template TKey of array-key
 * @template T
 */
class MyCollection implements \IteratorAggregate
{
    /**
     * @return T[]
     */
    public function getIterator(){
    }

    /**
     * @return T
     */
    public function getUser() {}
}