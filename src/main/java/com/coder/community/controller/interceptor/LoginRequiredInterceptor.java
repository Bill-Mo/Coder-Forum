package com.coder.community.controller.interceptor;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.coder.community.annotation.LoginRequired;
import com.coder.community.util.HostHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {
    
    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
                if (handler instanceof HandlerMethod) {
                    HandlerMethod handlerMethod = (HandlerMethod) handler;
                    Method method = handlerMethod.getMethod();
                    LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
                    if (loginRequired != null && hostHolder.getUser() == null) {
                        response.sendRedirect(request.getContextPath() + "/login");
                        return false;
                    }
                }
                return true;
    }
}
