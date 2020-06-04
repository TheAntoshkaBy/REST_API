package com.epam.second;

import com.epam.second.Utils;
import org.junit.Test;
import junit.framework.TestCase;
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

    @Test()// TODO: 4.06.20  
    public void isAllPositiveNumbers_InvalidData_(){
        Utils.isAllPositiveNumbers("fds","ff","fdsiu");
    }
}
