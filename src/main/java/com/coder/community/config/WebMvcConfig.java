package com.coder.community.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.coder.community.controller.interceptor.DataInterceptor;
import com.coder.community.controller.interceptor.LoginRequiredInterceptor;
import com.coder.community.controller.interceptor.LoginTicketInterceptor;
import com.coder.community.controller.interceptor.MessageInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
    
    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    // @Autowired
    // private LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    private MessageInterceptor messageInterceptor;

    @Autowired
    private DataInterceptor dataInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor)
        .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");
        
        // registry.addInterceptor(loginRequiredInterceptor)
        // .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

        registry.addInterceptor(messageInterceptor)
        .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

        registry.addInterceptor(dataInterceptor)
        .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");
	}
}
