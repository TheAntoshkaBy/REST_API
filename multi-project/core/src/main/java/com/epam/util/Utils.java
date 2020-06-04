package com.epam.util;

import java.util.Arrays;

public class Utils {
    public static boolean isAllPositiveNumbers(String... str){
        return  Arrays.stream(str).filter(x -> !StringUtils.isPositiveNumber(x)).findFirst().isEmpty();
    }
}
