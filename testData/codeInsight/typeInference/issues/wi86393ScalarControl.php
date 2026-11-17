<?php

class Bar {}

/** @return Closure(): Bar */
function scalarFactory(): Closure {}

<type value="Bar">scalarFactory()()</type>;
