<?php

class One {
  /** @return static<self, self> */
  public function getStatic() {}
}

<type value="One">$a</type> = Two::getOne()->getStatic();