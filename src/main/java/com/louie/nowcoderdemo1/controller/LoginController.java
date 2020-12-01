package com.louie.nowcoderdemo1.controller;

import com.google.code.kaptcha.Producer;
import com.louie.nowcoderdemo1.entity.User;
import com.louie.nowcoderdemo1.service.UserService;
import com.louie.nowcoderdemo1.utils.CommunityConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @RequestMapping(path = "/register",method = RequestMethod.GET)
    public String getRegister() {
        return "/site/register";
    }

    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String getLogin() {
        return "/site/login";
    }

    @RequestMapping(path = "/kaptcha",method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        //generate validation code
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        //store validation code in session
        session.setAttribute("kaptcha", text);

        //response to browser
        response.setContentType("image/png");
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            logger.error("验证码无响应。"+e.getMessage());
        }
    }

    @RequestMapping(path = "/register",method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> userMap = userService.register(user);
        if (userMap == null | userMap.isEmpty()) {
            model.addAttribute("msg", "注册成功，我们已经向您的邮箱发送了一封激活邮件，请尽快激活。");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg", userMap.get("usernameMsg"));
            model.addAttribute("passwordMsg", userMap.get("passwordMsg"));
            model.addAttribute("emailMsg", userMap.get("emailMsg"));
            return "/site/register";
        }

    }

    //http://localhost:8080/community/activation/userID/activationCode
    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) {
        int result = userService.activation(userId, code);
        if (result == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "激活成功，您的账号已经可以正常使用。");
            model.addAttribute("target", "/login");
        } else if (result == ACTIVATION_REPEAT) {
            model.addAttribute("msg", "您的账号已经激活，请不要重复激活。");
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("msg", "激活失败，您的激活码有误。");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password,String validationCode, boolean rememberMe,
                        HttpSession session, HttpServletResponse response, Model model) {
        //validate code
        String kaptcha = (String) session.getAttribute("kaptcha");
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(validationCode)
                || !validationCode.equalsIgnoreCase(kaptcha)) {
            model.addAttribute("codeMsg", "验证码有误。");
            return "/site/login";
        }

        //validate username and password
        int expiredSeconds = rememberMe ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> loginMap = userService.login(username, password, expiredSeconds);
        if (loginMap.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", loginMap.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        } else {
            model.addAttribute("usernameMsg", loginMap.get("usernameMsg"));
            model.addAttribute("passwordMsg", loginMap.get("passwordMsg"));
            return "/site/login";
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/login";
    }
}
