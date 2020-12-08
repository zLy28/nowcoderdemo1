package com.louie.nowcoderdemo1;

import com.louie.nowcoderdemo1.dao.DiscussPostMapper;
import com.louie.nowcoderdemo1.dao.LoginTicketMapper;
import com.louie.nowcoderdemo1.dao.UserMapper;
import com.louie.nowcoderdemo1.entity.DiscussPost;
import com.louie.nowcoderdemo1.entity.LoginTicket;
import com.louie.nowcoderdemo1.entity.User;
import com.louie.nowcoderdemo1.service.DiscussPostService;
import com.louie.nowcoderdemo1.utils.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class Nowcoderdemo1ApplicationTests {

    @Autowired
    private UserMapper userMapper1;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

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

    @Test
    public void insertTest() {
        User user = new User("zzzzly", "123", "abcd", "12345@qq.com",
                0, 0, "aisdhq123n", "sdnqwdnqd", new Date());
        userMapper1.insertUser(user);
        User zzzzly = userMapper1.selectByName("zzzzly");
        System.out.println(zzzzly);
    }

    @Test
    public void testLoginTicket() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(123);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));
        loginTicketMapper.insertLoginTicket(loginTicket);

        LoginTicket ticket = loginTicketMapper.getLoginTicket("abc");
        System.out.println(ticket);

        int abc = loginTicketMapper.updateLoginTicket("abc", 1);
        System.out.println(abc);
    }

    @Test
    public void testFilter() {
        String s = "我可以吸毒，我可以嫖 娼，我可以赌       博";
        String string = sensitiveFilter.filter(s);
        System.out.println(string);
    }

}
