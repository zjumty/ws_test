package org.devzen.ws_test;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * User: matianyi
 * Date: 15/2/17
 * Time: 下午6:08
 */
@Service
public class PriceServiceImpl implements PriceService {

    public static final BigDecimal BASE = new BigDecimal("1.132");

    @Override
    public Map<String, BigDecimal> getPrice(String[] priceIds) {
        Map<String, BigDecimal> prices = new HashMap<>();
        for (String id : priceIds) {
            int point = new Random().nextInt(150) - 100;
            prices.put(id, BASE.add(new BigDecimal(point).divide(new BigDecimal(100000), 5, RoundingMode.FLOOR)));
        }
        return prices;
    }
}
