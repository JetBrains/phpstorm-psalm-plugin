<?php
 class A
 {
     public int $b = 0;
     /**
      * @psalm-taint-source input
      * @psalm-taint-sink sql $sql
      * @psalm-taint-unescape html
      * @psalm-taint-escape ($escape is true ? 'html' : null)
      * @phpstan-assert-if-false int $this->test()
      * @phpstan-assert-if-true null $this->b
      */
     function getQ<caret>ueryParam(string $name): string {}

     function test() {}
 }