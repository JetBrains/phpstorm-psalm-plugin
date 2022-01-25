<?php

function f(){
  /** @var array{
   *   person: array{
   *      name?: Exception,
   *      age: int
   *     }
   *   } $test
   */
  $test = [];
  <type value="int|mixed">$test["person"]['age']</type>;
  <type value="Exception|mixed">$test["person"]['name']</type>;
  <type value="array|mixed">$test["person"]</type>;
}