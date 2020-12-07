package com.louie.nowcoderdemo1.controller;

import com.louie.nowcoderdemo1.annotation.NeedLogin;
import com.louie.nowcoderdemo1.entity.User;
import com.louie.nowcoderdemo1.service.UserService;
import com.louie.nowcoderdemo1.utils.CommunityUtil;
import com.louie.nowcoderdemo1.utils.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.domain}")
    private String domain;

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @NeedLogin
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getUserSettingPage() {
        return "/site/setting";
    }

    @NeedLogin
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadPicture(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "上传图片为空！");
            return "/site/setting";
        }

        String originalFilename = headerImage.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "上传图片格式不正确！");
            return "/site/setting";
        }

        String fileName = CommunityUtil.generateUUID() + suffix;
        File destination = new File(uploadPath + "/" + fileName);
        try {
            headerImage.transferTo(destination);
        } catch (IOException e) {
            logger.error("上传文件失败： " + e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常。", e);
        }

        //http://locahost:8080/community/user/header/xxxx.suffix
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);
        return "redirect:/index";
    }

    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(HttpServletResponse response, @PathVariable("fileName") String fileName) {
        //get pictures on server
        fileName = uploadPath + "/" + fileName;
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        response.setContentType("image/" + suffix);
        try (
                ServletOutputStream outputStream = response.getOutputStream();
                FileInputStream fis = new FileInputStream(fileName);
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                outputStream.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败" + e.getMessage());
        }
    }

    @NeedLogin
    @RequestMapping(path = "/changeCode", method = RequestMethod.POST)
    public String changeCode(String oldPassword, String newPassword, Model model) {
        User user = hostHolder.getUser();
        Map<String, Object> map = userService.changePassword(oldPassword, newPassword, user.getId());
        if (map == null || map.isEmpty()) {
            return "redirect:/logout";
        }else {
            model.addAttribute("oldPasswordMsg", map.get("oldPasswordMsg"));
            model.addAttribute("newPasswordMsg", map.get("newPasswordMsg"));
            return "/site/setting";
        }
    }
}
