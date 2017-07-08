<?php

namespace App\Helper;

abstract class BasicEnum {
    private static $constCacheArray = NULL;

    private static function getConstants() {
        if (self::$constCacheArray == NULL) {
            self::$constCacheArray = [];
        }
        $calledClass = get_called_class();
        if (!array_key_exists($calledClass, self::$constCacheArray)) {
            $reflect = new ReflectionClass($calledClass);
            self::$constCacheArray[$calledClass] = $reflect->getConstants();
        }
        return self::$constCacheArray[$calledClass];
    }

    public static function isValidName($name, $strict = false) {
        $constants = self::getConstants();

        if ($strict) {
            return array_key_exists($name, $constants);
        }

        $keys = array_map('strtolower', array_keys($constants));
        return in_array(strtolower($name), $keys);
    }

    public static function isValidValue($value, $strict = true) {
        $values = array_values(self::getConstants());
        return in_array($value, $values, $strict);
    }
}

abstract class Operator extends BasicEnum {
    const BETWEEN = 'between';
    const EQUAL = '=';
    const GREATER_THAN = '>';
    const GREATER_THAN_EQUAL = '>=';
    const INSENSITIVE_LIKE = 'ilike';
    const LESS_THAN = '<';
    const LESS_THAN_EQUAL = '<=';
    const LIKE = 'like';
    const NOT_EQUAL = '<>';
    const NOT_LIKE = 'not like';
    const NOT = '!=';
}