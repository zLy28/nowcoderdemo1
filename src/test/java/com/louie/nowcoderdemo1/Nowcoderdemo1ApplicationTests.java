package com.louie.nowcoderdemo1;

import com.louie.nowcoderdemo1.dao.DiscussPostMapper;
import com.louie.nowcoderdemo1.dao.UserMapper;
import com.louie.nowcoderdemo1.entity.DiscussPost;
import com.louie.nowcoderdemo1.entity.User;
import com.louie.nowcoderdemo1.service.DiscussPostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class Nowcoderdemo1ApplicationTests {

    @Autowired
    private UserMapper userMapper1;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private DiscussPostService discussPostService;

    @Test
    void contextLoads() {
        List<DiscussPost> discussPosts = discussPostService.getDiscussPost(0, 0, 10);
        for (DiscussPost post:discussPosts
             ) {
            System.out.println(post);
        }
        int sum = discussPostMapper.countDiscussPosts(0);
        System.out.println(sum);

    }



}
