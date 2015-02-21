package org.devzen.ws_test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: matianyi
 * Date: 15/2/17
 * Time: 下午6:06
 */
public interface PriceService {
    /**
     * 取价格
     * @param priceIds 价格ID的列表
     * @return 价格key，value对
     */
    Map<String, BigDecimal> getPrice(String[] priceIds);

    List<CandleStickData> getPriceDatas(String name, Date from, Date to);
}
