package com.epam.util;

import org.apache.commons.lang3.math.NumberUtils;

public class StringUtils {
    public static boolean isPositiveNumber(String str) {
        if (!NumberUtils.isCreatable(str)) {
            throw new NumberFormatException();
        }
        return NumberUtils.toDouble(str) >= 0;
    }
}