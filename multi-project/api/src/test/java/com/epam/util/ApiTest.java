package com.epam.util;

import org.junit.Test;
import org.junit.Assert;

public class ApiTest {

    @Test
    public void allPositive(){
        Assert.assertTrue( Utils.isAllPositiveNumbers("32", "43", "432432", "432") );
    }

    @Test
    public void oneNegative(){
        Assert.assertFalse( Utils.isAllPositiveNumbers("32","-43","432432","432") );
    }

    @Test(expected = NumberFormatException.class)
    public void isAllPositiveNumbers_InvalidData_Exception(){
        Utils.isAllPositiveNumbers("fds","ff","fdsiu");
    }
}
