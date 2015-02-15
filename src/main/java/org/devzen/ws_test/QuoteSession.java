package org.devzen.ws_test;

import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;
import java.util.Map;

/**
 * User: matianyi
 * Date: 15/2/15
 * Time: 下午10:32
 */
public class QuoteSession {
    private Session connection;
    private Map<String, Object> attributes = new HashMap<>();

    public QuoteSession(Session connection) {
        this.connection = connection;
    }


}
