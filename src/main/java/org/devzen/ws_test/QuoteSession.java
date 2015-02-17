package org.devzen.ws_test;

import org.eclipse.jetty.websocket.api.Session;

import java.util.*;

/**
 * User: matianyi
 * Date: 15/2/15
 * Time: 下午10:32
 */
public class QuoteSession {
    private Session connection;
    private Map<String, Object> attributes = new HashMap<>();

    // ms
    private volatile long nextTick;

    // ms
    private int quoteInterval;

    // 要询价的价格ID
    private volatile String[] priceIds;

    // UUID
    private String id;

    public QuoteSession(Session connection, int quoteInterval) {
        this.id = UUID.randomUUID().toString();
        this.connection = connection;
        this.nextTick = System.currentTimeMillis();
        this.quoteInterval = quoteInterval;
    }

    public Session getConnection() {
        return connection;
    }

    public void setAttribute(String key, Object object) {
        attributes.put(key, object);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * 服务过一次
     */
    public void served(){
        nextTick = nextTick + quoteInterval;
    }

    public long getNextTick(){
        return nextTick;
    }

    public String[] getPriceIds() {
        return priceIds;
    }

    public void setPriceIds(String[] priceIds) {
        this.priceIds = priceIds;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuoteSession session = (QuoteSession) o;

        if (!id.equals(session.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder("QuoteSession{");
        sb.append("id='").append(id).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
