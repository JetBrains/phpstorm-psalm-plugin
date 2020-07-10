<?php
namespace K{
    class C{
        public function f()
        {
            return 1;
        }
    }
}

namespace N {


    /**
     * @param array<int, <caret>\K\C> $arr
     */
    function takesArray(array $arr): void
    {
        if ($arr) {
            echo ($arr[0])->f();
        }
    }
}