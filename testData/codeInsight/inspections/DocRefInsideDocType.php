<?php

/**
 * @psalm-param <info descr="PHP_DOC_IDENTIFIER">C::A_1</info>|<info descr="PHP_DOC_IDENTIFIER">C::A_2</info> <info descr="PHP_DOC_VAR">$a</info>
 * @psalm-return <info descr="PHP_DOC_IDENTIFIER">C::A_1</info>|<info descr="PHP_DOC_IDENTIFIER">C::A_2</info>
 */
function <info descr="PHP_FUNCTION">f</info>(<info descr="PHP_PARAMETER">$a</info>, <info descr="PHP_PARAMETER">$b</info>)
{
    /** @psalm-var <info descr="PHP_DOC_IDENTIFIER">C::A_*</info> $c */
    $c;
}