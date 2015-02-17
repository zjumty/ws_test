package org.devzen.ws_test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.eclipse.jetty.websocket.common.io.FutureWriteCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 报价的WebSocket服务端
 * User: matianyi
 * Date: 15/2/15
 * Time: 下午10:22
 */
@Component("quoteSocket")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QuoteSocket implements WebSocketListener {
    private static final Logger LOG = LoggerFactory.getLogger(QuoteSocket.class);

    @Autowired
    private SessionManagerImpl sessionManager;

    @Autowired
    private PriceProducer priceProducer;

    private QuoteSession session;

    public void onWebSocketBinary(byte payload[], int offset, int len) {

    }


    public void onWebSocketClose(int statusCode, String reason) {
        LOG.info("WebSocket Close");
    }


    public void onWebSocketConnect(Session connection) {
        session = new QuoteSession(connection, 200);
        sessionManager.registerSession(session);
        LOG.info("WebSocket Connect {}", connection.getRemoteAddress());
        connection.getRemote().sendString("{result:'OK'}", new FutureWriteCallback() {
            @Override public void writeSuccess() {
                LOG.info("writeSuccess");
                priceProducer.appendQuoteTask(session);
            }
        });
    }

    public void onWebSocketError(Throwable cause) {

    }

    public void onWebSocketText(String message) {
        LOG.info("received:{}", message);
        //TODO: parseJson
        try {
            JSONObject root = JSON.parseObject(message);
            if ("set_price_ids".equals(root.getString("action"))) {
                JSONArray datas = root.getJSONArray("data");
                String[] priceIds = new String[datas.size()];
                for (int i = 0; i < datas.size(); i++) {
                    priceIds[i] = datas.getString(i);
                }
                session.setPriceIds(priceIds);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
