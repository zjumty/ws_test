package org.devzen.ws_test;

import org.omg.PortableInterceptor.Interceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * User: matianyi
 * Date: 15/2/15
 * Time: 下午11:11
 */
public class AutoModelVariableInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
        if (mav != null && mav.getModel() != null) {
            Map<String, Object> model = mav.getModel();
            model.put("base", request.getContextPath());
            model.put("request", request);
            model.put("session", request.getSession());
            model.put("application", request.getServletContext());

        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
