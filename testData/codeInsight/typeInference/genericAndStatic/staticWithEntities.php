<?php

/** @template T */
class Entities
{
  /** @return EntityList<static> */
  static function where(){}
}

class Users extends Entities {}

class Occupations extends Entities {}

/** @template T */
class EntityList
{
  /** @return T */
  function first() {}
}

<type value="mixed|Users">Users::where()->first()</type>;
<type value="mixed|Occupations">Occupations::where()->first()</type>;
