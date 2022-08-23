<?php

class Person{}

/**
 * @extends GenericRepo<Person>
 */
class PersonRepo extends GenericRepo {}

/**
 * @template T
 */
class GenericRepo
{
    /**
     * @return T
     */
    public function save($entity)
    {
        //do something
        return $entity;
    }
}

class RepositoryHelper{
    /**
     * @return PersonRepo
     */
    public function getPersonRepository()
    {
        return new PersonRepo();
    }
}

$repo = new PersonRepo();
<type value="Person">$res</type> = $repo->save();

$repo = (new RepositoryHelper())->getPersonRepository();
<type value="Person">$res</type> = $repo->save();
