<?php

class User {
}

/** @return array{1: string, "2": int} */
function a(): array {
  return [];

}

<type value="string|mixed">a()[1]</type>;
<type value="mixed">a()["1"]</type>;
<type value="int|mixed">a()["2"]</type>;
<type value="mixed">a()[2]</type>;

