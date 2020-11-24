package com.louie.nowcoderdemo1.service;

import com.louie.nowcoderdemo1.dao.DiscussPostMapper;
import com.louie.nowcoderdemo1.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> getDiscussPost(int userId, int offset, int limit) {
       return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    public int countPosts(int userId) {
        return discussPostMapper.countDiscussPosts(userId);
    }
}
