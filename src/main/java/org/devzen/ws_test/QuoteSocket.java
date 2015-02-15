package org.devzen.ws_test;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 报价的WebSocket服务端
 * User: matianyi
 * Date: 15/2/15
 * Time: 下午10:22
 */
@Component
public class QuoteSocket implements WebSocketListener {
    private static final Logger LOG = LoggerFactory.getLogger(QuoteSocket.class);

    @Autowired
    private SessionManager sessionManager;

    public void onWebSocketBinary(byte payload[], int offset, int len){

    }


    public void onWebSocketClose(int statusCode, String reason){
        LOG.info("WebSocket Close");
    }


    public void onWebSocketConnect(Session session){
        LOG.info("WebSocket Connect {}", session.getRemoteAddress());
    }

    public void onWebSocketError(Throwable cause){

    }

    public void onWebSocketText(String message){

    }
}
