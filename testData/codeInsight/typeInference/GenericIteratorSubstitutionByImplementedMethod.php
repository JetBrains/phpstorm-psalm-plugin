<?php
namespace A;

interface I {
  public function foo(): void;
}

class C implements I {
  public function foo(): void {}
}

interface ISeq extends \IteratorAggregate {
  /**
   * @return Iterator<I>
   */
  public function getIterator(): \Iterator;
}

function walkISeq(ISeq $seq) {
  foreach ($seq as $value) {
    <type value="\A\I|mixed">$value</type>;
  }
}

namespace B;

interface I {
  public function foo(): void;
}

class C implements I {
  public function foo(): void {}
}
/**
* @template T
*/
interface ISeq extends \IteratorAggregate {
  /**
   * @return Iterator<T>
   */
  public function getIterator(): \Iterator;
}

/**
* @param ISeq<I> $seq
*/
function walkISeq(ISeq $seq) {
  foreach ($seq as $value) {
    <type value="\B\I|mixed|T">$value</type>;
  }
}