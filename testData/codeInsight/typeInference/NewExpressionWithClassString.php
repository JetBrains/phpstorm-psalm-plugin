<?php

class Foo {}

/** @var class-string<Foo> $klass */
$klass = null;
$klassInstance = new $klass();
<type value="mixed|Foo">$klassInstance</type>
