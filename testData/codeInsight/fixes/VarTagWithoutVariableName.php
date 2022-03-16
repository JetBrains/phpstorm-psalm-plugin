<?php

function foo($prop)
{
    /** <caret>@psalm-var stdClass */
    $a = $prop->getStdClass();
}
