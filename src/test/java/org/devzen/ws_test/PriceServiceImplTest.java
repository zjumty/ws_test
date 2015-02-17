package org.devzen.ws_test;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.Assert.*;

public class PriceServiceImplTest {

    @Test
    public void testGetPrices(){
        PriceServiceImpl service = new PriceServiceImpl();
        Map<String, BigDecimal> prices = service.getPrice(new String[]{"EUR/USD"});
        assertEquals(1, prices.size());
        assertNotNull(prices.get("EUR/USD"));
        System.out.println(prices.get("EUR/USD"));
    }

}