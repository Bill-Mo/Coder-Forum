package com.coder.community.util;

import org.springframework.stereotype.Component;

import com.coder.community.entity.User;

@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}

