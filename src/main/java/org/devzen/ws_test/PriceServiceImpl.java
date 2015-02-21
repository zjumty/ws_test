package org.devzen.ws_test;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * User: matianyi
 * Date: 15/2/17
 * Time: 下午6:08
 */
@Service
public class PriceServiceImpl implements PriceService {

    public static final BigDecimal BASE = new BigDecimal("1.132");

    private static BigDecimal getRandomPrice() {
        return getRandomPrice(BASE);
    }

    private static BigDecimal getRandomPrice(BigDecimal base) {
        int point = new Random().nextInt(2000) - 1000;
        return base.add(new BigDecimal(point).divide(new BigDecimal(100000), 5, RoundingMode.FLOOR));
    }

    @Override
    public Map<String, BigDecimal> getPrice(String[] priceIds) {
        Map<String, BigDecimal> prices = new HashMap<>();
        for (String id : priceIds) {
            prices.put(id, getRandomPrice());
        }
        return prices;
    }

    @Override
    public List<CandleStickData> getPriceDatas(String name, Date from, Date to) {
        List<CandleStickData> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            CandleStickData data = new CandleStickData();
            data.setName(name);
            BigDecimal close = getRandomPrice();
            BigDecimal open = getRandomPrice();
            BigDecimal high = MathUtils.max(open, close, getRandomPrice(new BigDecimal("1.142")));
            BigDecimal low = MathUtils.min(open, close, getRandomPrice(new BigDecimal("1.122")));
            data.setClose(close);
            data.setHigh(high);
            data.setLow(low);
            data.setOpen(open);
            data.setVolume(10000 + (new Random().nextInt(5000)));
            data.setTime(String.format("13:%02d", (i + 1)));
            datas.add(data);
        }
        return datas;
    }
}
