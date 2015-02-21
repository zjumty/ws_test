package org.devzen.ws_test;

import java.math.BigDecimal;

/**
 * User: matianyi
 * Date: 15/2/21
 * Time: 上午11:41
 */
public class MathUtils {

    /**
     * 返回数字中最大的
     *
     * @param nums 要计算的数字
     * @param <T>  数字的类型
     * @return 最大的数字
     */
    @SafeVarargs public static <T extends Comparable<T>> T max(T... nums) {
        if (nums == null || nums.length == 0) {
            return null;
        }

        T max = nums[0];
        for (T num : nums) {
            if (max.compareTo(num) < 0) {
                max = num;
            }
        }
        return max;
    }

    /**
     * 返回数字中最小的
     *
     * @param nums 要计算的数字
     * @param <T>  数字的类型
     * @return 最小的数字
     */
    @SafeVarargs public static <T extends Comparable<T>> T min(T... nums) {
        if (nums == null || nums.length == 0) {
            return null;
        }

        T min = nums[0];
        for (T num : nums) {
            if (min.compareTo(num) > 0) {
                min = num;
            }
        }
        return min;
    }
}
