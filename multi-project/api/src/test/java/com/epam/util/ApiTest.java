package com.epam.util;

import com.epam.second.Utils;
import org.junit.Assert;
import org.junit.Test;

public class ApiTest {

    @Test
    public void isAllPositiveNumbers_InvalidData_True() {
        Assert.assertTrue(Utils.isAllPositiveNumbers("32", "43", "432432", "432"));
    }

    @Test
    public void isAllPositiveNumbers_InvalidData_False() {
        Assert.assertFalse(Utils.isAllPositiveNumbers("32", "-43", "432432", "432"));
    }

    @Test(expected = NumberFormatException.class)
    public void isAllPositiveNumbers_InvalidData_Exception() {
        Utils.isAllPositiveNumbers("fds", "ff", "fdsiu");
    }
}
