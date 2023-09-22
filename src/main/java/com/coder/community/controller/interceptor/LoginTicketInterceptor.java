package com.coder.community.controller.interceptor;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.coder.community.entity.LoginTicket;
import com.coder.community.entity.User;
import com.coder.community.service.UserService;
import com.coder.community.util.CookieUtil;
import com.coder.community.util.HostHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    
    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
                String ticket = CookieUtil.getValue(request, "ticket");
                if (ticket != null) {
                    // Get ticket
                    LoginTicket loginTicket = userService.findlLoginTicket(ticket);
                    // Check validity of ticket
                    if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                        // Search for user
                        User user = userService.findUserById(loginTicket.getUserId());
                        // Save user status
                        hostHolder.setUser(user);
                        // Save user authentication
                        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), userService.getAuthorities(user.getId()));
                        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
                    }
                }
                return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
                hostHolder.clear();
                SecurityContextHolder.clearContext();
	}
}
