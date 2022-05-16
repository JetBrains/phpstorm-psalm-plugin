<?php

class UserCL
{
    /**
     * @var array{
     *          first: string,
     *          last: string
     *        } $name
     * @param array{
     *           email: string,
     *           phone: string
     *        } $contact
     */
    public function __construct($name, $contact)
    {
    }
}

$ucl = new UserCL(['<caret>'],['']);
