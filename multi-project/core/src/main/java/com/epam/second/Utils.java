package com.epam.second;

import com.epam.util.StringUtils;

public class Utils {
    public static boolean isAllPositiveNumbers(String... str){
// TODO: 4.06.20 стрим 
        for(int i = 0; i < str.length; i++){
            if(!StringUtils.isPositiveNumber(str[i])){
                return false;
            }
        }
        return true;
    }
}
