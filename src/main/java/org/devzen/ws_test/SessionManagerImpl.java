package org.devzen.ws_test;

import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * User: matianyi
 * Date: 15/2/15
 * Time: 下午10:31
 */
@Component
public class SessionManagerImpl implements SessionManager {
    private static final Logger LOG = LoggerFactory.getLogger(SessionManagerImpl.class);

    private ConcurrentMap<Session, QuoteSession> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void registerSession(QuoteSession session) {
        sessionMap.put(session.getConnection(), session);
    }

    public void unRegisterSession(QuoteSession session) {
        sessionMap.remove(session.getConnection());
    }

    public QuoteSession getSession(Session connection){
        return sessionMap.get(connection);
    }

    public Collection<QuoteSession> getAllSessions(){
        return Collections.unmodifiableCollection(sessionMap.values());
    }

    public void serveSession(SessionExecutor executor){
        //QuoteSession session = queue.p
    }
}
