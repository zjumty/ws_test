package org.devzen.ws_test;

import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.eclipse.jetty.websocket.servlet.*;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.annotation.WebServlet;

/**
 * User: matianyi
 * Date: 15/2/15
 * Time: 下午10:36
 */
@SuppressWarnings("serial")
@WebServlet(name = "Quote WebSocket Servlet", urlPatterns = { "/quote" })
public class QuoteServlet extends WebSocketServlet {

    @Override public void configure(WebSocketServletFactory factory) {
        final ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
        if(applicationContext == null){
            throw new RuntimeException("找不到Spring Context。");
        }
        factory.setCreator(new WebSocketCreator() {
            @Override
            public Object createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) {
                return applicationContext.getBean("quoteSocket", WebSocketListener.class);
            }
        });
    }
}
