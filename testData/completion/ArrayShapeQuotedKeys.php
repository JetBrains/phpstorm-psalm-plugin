<?php

/**
 * @param array{
 *           "a": int,
 *           ?'b': int,
 *           ?'c' : int,
 *           ?"d" : int,
 *           "f" : int,
 *           "g": int
 *        } $a
 *
 */
function f($a) {
  $a['<caret>']
}


