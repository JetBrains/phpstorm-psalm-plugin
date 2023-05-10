<?php

class Base {}

/**
 * @template TNew
 * @template TNew2 of Base
 * @template-covariant TNew3
 * @template-contravariant TNew4
 * @psalm-template-covariant TNew5 of Base
 * @psalm-template-contravariant TNew6 of Base
 */
function fo<caret>o() {}
