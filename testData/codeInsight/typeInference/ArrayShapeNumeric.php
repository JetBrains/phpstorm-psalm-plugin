<?php

class User {
}

/** @return array{1: string, "2": int, "02": bool} */
function a(): array {
  return [];

}

<type value="string|mixed">a()[1]</type>;
<type value="string|mixed">a()["1"]</type>;
<type value="int|mixed">a()["2"]</type>;
<type value="int|mixed">a()[2]</type>;
<type value="string|mixed">a()['1']</type>;
<type value="bool|mixed">a()["02"]</type>;
