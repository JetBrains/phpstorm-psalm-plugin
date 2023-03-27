<?php

/**
 * @template T
 */
class EntityRepository
{
  /**
   * @return T
   */
  public function findOneBy()
  {
  }
}

interface EntityManagerInterface
{
  /**
   * @param class-string<T> $className
   *
   * @return EntityRepository<T>
   *
   * @template T of object
   */
  public function getRepository($className);
}

class ExceptionSubscriber
{
  /**
   * @var EntityManagerInterface
   */
  private $em;

  public function test()
  {
    $model = $this->em->getRepository(Exception::class)->findOneBy();
    <type value="Exception">$model</type>;
  }
}
