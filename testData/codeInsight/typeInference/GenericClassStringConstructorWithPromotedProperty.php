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
  public function __construct(
    /** @var class-string<M> */
    protected string $modelClass
  )
  {
    $modelClass->foo();
  }

  /** @return M|null */
  public function get()
  {
    return new $this->modelClass();
  }
}

$repo = new ModelRepo(User::class);
$user = $repo->get();
<type value="null|User">$user</type>
