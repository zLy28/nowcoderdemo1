package com.louie.nowcoderdemo1.utils;

import com.louie.nowcoderdemo1.entity.User;
import org.springframework.stereotype.Component;

/**
 * replace session to hold data
 */

@Component
public class HostHolder {
    private ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public void setUser(User user) {
        threadLocal.set(user);
    }

    public User getUser() {
        return threadLocal.get();
    }

    public void clear() {
        threadLocal.remove();
    }
}
