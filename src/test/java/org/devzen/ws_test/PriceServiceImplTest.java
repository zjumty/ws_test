package org.devzen.ws_test;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class PriceServiceImplTest {

    @Test
    public void testGetPrice(){
        PriceServiceImpl service = new PriceServiceImpl();
        Map<String, BigDecimal> prices = service.getPrice(new String[]{"EUR/USD"});
        assertEquals(1, prices.size());
        assertNotNull(prices.get("EUR/USD"));
        System.out.println(prices.get("EUR/USD"));
    }

    @Test
    public void testGetPriceDatas() throws Exception {
        PriceServiceImpl service = new PriceServiceImpl();
        List<CandleStickData> datas = service.getPriceDatas("EUR/USD", null, null);
        assertEquals(20, datas.size());
        CandleStickData data = datas.get(0);
        System.out.println("close:" + data.getClose());
        System.out.println("open:" + data.getOpen());
        System.out.println("high:" + data.getHigh());
        System.out.println("low:" + data.getLow());
        System.out.println("name:" + data.getName());
        System.out.println("time:" + data.getTime());
        System.out.println("volume:" + data.getVolume());
    }
}