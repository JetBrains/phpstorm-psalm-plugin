<?php

class User
{
  public $name = 'Joe';
}

/**
 * @template M
 */
class ModelRepo
{
  /** @var class-string<M> */
  protected $modelClass;

  /** @param class-string<M> $modelClass */
  public function __construct(string $modelClass)
  {
    $this->modelClass = $modelClass;
  }

  /** @return M|null */
  public function get()
  {
    return new $this->modelClass();
  }
}

$user = (new ModelRepo(User::class))->get();
<type value="null|mixed|User">$user</type>
