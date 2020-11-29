package com.louie.nowcoderdemo1.service;

import com.louie.nowcoderdemo1.dao.UserMapper;
import com.louie.nowcoderdemo1.entity.User;
import com.louie.nowcoderdemo1.utils.CommunityConstant;
import com.louie.nowcoderdemo1.utils.CommunityUtil;
import com.louie.nowcoderdemo1.utils.MailFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private MailFunction mailFunction;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }

        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空！");
        }

        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空！");
        }

        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空！");
        }

        //compared registered info with data in the database
        User userInDatabase = userMapper.selectByName(user.getUsername());
        if (userInDatabase != null) {
            map.put("usernameMsg", "账号已经存在！");
            return map;
        }

        userInDatabase = userMapper.selectByEmail(user.getEmail());
        if (userInDatabase != null) {
            map.put("emailMsg", "邮箱已经存在！");
            return map;
        }

        //register user into db
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setStatus(0);
        user.setType(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        //send activation email
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        //url format:
        //http://localhost:8080/community/activation/userID/activationCode
        context.setVariable("url", domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode());
        String process = templateEngine.process("/mail/activation", context);
        mailFunction.sendMail(user.getEmail(), "账号激活", process);

        return map;
    }

    public int activation(int id, String code) {
        User user = userMapper.selectById(id);
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(id, 1);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }
}
