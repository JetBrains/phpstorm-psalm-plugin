<?php

declare(strict_types=1);

use Ds\Set;

/** @var Set<string> $set */
$set = new Set();

use Doctrine\Common\Collections\ArrayCollection;

/** @var ArrayCollection<int, \Exception> $array */ //@var tag specifies the type already inferred from source code
$array = new ArrayCollection();
$array[] = new \Exception();