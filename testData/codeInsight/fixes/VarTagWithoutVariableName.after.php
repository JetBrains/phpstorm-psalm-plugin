<?php

function foo($prop)
{
    /** <caret>@psalm-var stdClass $a */
    $a = $prop->getStdClass();
}
