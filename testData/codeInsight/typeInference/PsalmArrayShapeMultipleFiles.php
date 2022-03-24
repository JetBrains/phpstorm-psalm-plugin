<?php


$ff = ff();
<type value="mixed|Exception">$ff['age']</type>;
<type value="int|mixed|Exception">$ff[$x]</type>;
<type value="mixed|Exception">ff1()[$x]['age']</type>;
<type value="int|mixed|Exception">ff1()[$x][$x]</type>;

<type value="mixed|Exception">AA::ff1()['age']</type>;
<type value="mixed|Exception">(new AA)->ff1()['age']</type>;