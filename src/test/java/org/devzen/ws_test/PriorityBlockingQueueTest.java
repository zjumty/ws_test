package org.devzen.ws_test;

import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * User: matianyi
 * Date: 15/2/16
 * Time: 下午10:03
 */
public class PriorityBlockingQueueTest {
    @Test
    public void testOrder() throws InterruptedException {
        PriorityBlockingQueue<Integer> q = new PriorityBlockingQueue<>(5, new Comparator<Integer>() {
            @Override public int compare(Integer lhs, Integer rhs) {
                return lhs - rhs;
            }
        });
        q.put(2);
        q.put(1);
        q.put(3);

        Assert.assertEquals(1, q.take().intValue());
        Assert.assertEquals(2, q.size());
        Assert.assertEquals(2, q.take().intValue());
        Assert.assertEquals(1, q.size());
        Assert.assertEquals(3, q.take().intValue());
        Assert.assertEquals(0, q.size());
    }
}
