package com.coder.community.service;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class alphaService {
    public alphaService() {
        System.out.println("This is service");
    }

    @PostConstruct
    public void init() {
        System.out.println("Initialize service...");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Destroy service...");
    }
}
