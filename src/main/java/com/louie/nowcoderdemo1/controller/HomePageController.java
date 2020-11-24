package com.louie.nowcoderdemo1.controller;

import com.louie.nowcoderdemo1.entity.DiscussPost;
import com.louie.nowcoderdemo1.entity.Page;
import com.louie.nowcoderdemo1.entity.User;
import com.louie.nowcoderdemo1.service.DiscussPostService;
import com.louie.nowcoderdemo1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomePageController {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    public String getHomePage(Model model, Page page) {
        page.setTotalNumOfPost(discussPostService.countPosts(0));
        page.setPath("/index");
        List<DiscussPost> discussPost = discussPostService.getDiscussPost(0, page.getOffset(), page.getNumOfPostPerPage());
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (discussPost != null) {
            for (DiscussPost post : discussPost
            ) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                User user = userService.findUserById(post.getUserId());
                map.put("user", user);
                mapList.add(map);
            }
        }
        model.addAttribute("discussPosts", mapList);
        return "/index";
    }

}
