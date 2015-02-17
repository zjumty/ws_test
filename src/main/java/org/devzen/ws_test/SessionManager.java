package org.devzen.ws_test;

/**
 * User: matianyi
 * Date: 15/2/16
 * Time: 下午5:56
 */
public interface SessionManager {
    void registerSession(QuoteSession session);
    void serveSession(SessionExecutor executor);
}
