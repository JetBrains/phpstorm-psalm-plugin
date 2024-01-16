<?php

class User {

  public $name = 'Joe';

  public static function getId(): int {
  }
}

/**
 * @template M
 */
class ModelRepo {
  /**
   * @var class-string<M>
   **/
  public $modelClass;

  /** @param class-string<M> $modelClass */
  public function __construct($modelClass) {
    $this->modelClass = $modelClass;
  }
}

$user = (new ModelRepo(User::class))->modelClass;
<type value="mixed|int">$user::getId()</type>;
