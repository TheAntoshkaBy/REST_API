package com.epam.util;

import org.apache.commons.lang3.math.NumberUtils;

public class StringUtils {
    public static boolean isPositiveNumber(String str){
        int totalNumber = NumberUtils.toInt(str);
        if(totalNumber == 0 && !Integer.toString(totalNumber).equals(str)){
            throw new NumberFormatException();
        }
        return totalNumber >= 0;
    }
}
