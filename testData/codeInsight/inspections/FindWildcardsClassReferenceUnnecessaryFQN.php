<?php
namespace First;
class LogLevel
{
    const EMERGENCY = 'emergency';
    const ALERT     = 'alert';
}

class Test {

    /**
     * @param  <weak_warning descr="Qualifier is unnecessary and can be removed">\First\</weak_warning>LogLevel::* $fatalLevel // FQN not highlighted
     */
    public static function register($errorLevelMap, $fatalLevel = null) {
    }
}
