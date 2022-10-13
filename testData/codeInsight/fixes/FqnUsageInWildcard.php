<?php
namespace First;
class LogLevel
{
    const EMERGENCY = 'emergency';
    const ALERT     = 'alert';
}
namespace x;
use First\LogLevel;
class Test {

    /**
     * @param  <caret>\First\LogLevel::* $fatalLevel
     */
    public static function register($errorLevelMap, $fatalLevel = null) {
    }
}
