package org.devzen.ws_test;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.*;

/**
 * User: matianyi
 * Date: 15/2/15
 * Time: 下午10:31
 */
@Component
public class PriceProducer {
    private final static Logger LOG = LoggerFactory.getLogger(PriceProducer.class);

    private final static int MAX_WAITING_COUNT = 100000;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private PriceService priceService;

    @Autowired
    @Qualifier("quoteExecutor")
    private ExecutorService executor;

    private WaitingMonitor waitingMonitor;

    // 等待执行的session，按时间排序，小的在前
    private PriorityBlockingQueue<QuoteSession> waitingQueue = new PriorityBlockingQueue<>(MAX_WAITING_COUNT, new Comparator<QuoteSession>() {
        @Override public int compare(QuoteSession lhs, QuoteSession rhs) {
            return (int) (lhs.getNextTick() - rhs.getNextTick());
        }
    });

    private ExecuteResult sendPrices(QuoteSession session) {
        if (!session.getConnection().isOpen()) {
            return ExecuteResult.REMOVE;
        } else {
            try {
                if (session.getPriceIds() != null && session.getPriceIds().length > 0) {
                    Map<String, BigDecimal> prices = priceService.getPrice(session.getPriceIds());
                    session.getConnection().getRemote().sendString(JSON.toJSONString(prices));
                }
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
            return ExecuteResult.CONTINUE;
        }
    }

    /**
     * 添加一个报价的任务
     *
     * @param session 报价服务的Session
     */
    public void appendQuoteTask(final QuoteSession session) {
        executor.submit(new QuoteRunner(session));
    }

    /**
     * 添加一个Session到
     *
     * @param session 报价服务的Session
     */
    private void appendWaitingTask(QuoteSession session) {
        session.served();
        waitingQueue.put(session);
    }

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        LOG.debug("PriceProducer init");
        waitingMonitor = new WaitingMonitor();
        waitingMonitor.start();
    }

    @PreDestroy
    public void destroy() {
        executor.shutdownNow();
        waitingMonitor.interrupt();
    }


    class WaitingMonitor extends Thread {
        public WaitingMonitor() {
            this.setName("WaitingMonitor");
        }

        @Override
        public void run() {
            LOG.info("WaitingMonitor started.");
            while (!isInterrupted()) {
                try {
                    QuoteSession session = waitingQueue.take();
                    long remainTime = session.getNextTick() - System.currentTimeMillis();
                    if (remainTime > 0) {
                        Thread.sleep(remainTime);
                    }
                    // 如果Session没有断开，继续提供报价服务。
                    if(session.getConnection().isOpen()){
                        appendQuoteTask(session);
                    }

                } catch (InterruptedException e) {
                    LOG.info(e.getMessage(), e);
                    break;
                }
            }
            LOG.info("WaitingMonitor stopped.");
        }
    }

    class QuoteRunner implements Runnable {

        private QuoteSession session;

        public QuoteRunner(QuoteSession session) {
            this.session = session;
        }

        @Override
        public void run() {
            ExecuteResult result = sendPrices(session);
            if (result == ExecuteResult.CONTINUE) {
                appendWaitingTask(session);
            }
        }
    }
}
