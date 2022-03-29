<?php
class DD extends Base {
    /**
     * @param Promise<Bar> $f
     * @return Foo|Generator
     */
    function ff($f) {
        <type value="Foo">yield f()</type>;
        <type value="Bar">yield $f</type>;
        <type value="Bar">yield $this->a</type>;
    }

}