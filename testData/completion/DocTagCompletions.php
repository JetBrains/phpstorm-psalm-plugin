<?php

namespace psalmCompletions;

interface Bar {
}

/**
 * @psa<caret>
 */
trait Foo {
}


class Lorem implements Bar {
  use Foo;
}