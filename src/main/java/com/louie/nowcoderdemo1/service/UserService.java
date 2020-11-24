package com.louie.nowcoderdemo1.service;

import com.louie.nowcoderdemo1.dao.UserMapper;
import com.louie.nowcoderdemo1.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }
}
