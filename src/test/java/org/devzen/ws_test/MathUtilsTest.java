package org.devzen.ws_test;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testMax() throws Exception {
        BigDecimal num = MathUtils.max(new BigDecimal("1.1"), new BigDecimal("1.3"), new BigDecimal("1.11")) ;
        Assert.assertEquals(num, new BigDecimal("1.3"));
    }

    @Test
    public void testMin() throws Exception {
        BigDecimal num = MathUtils.min(new BigDecimal("1.1"), new BigDecimal("1.01"), new BigDecimal("1.11")) ;
        Assert.assertEquals(num, new BigDecimal("1.01"));
    }
}