package com.louie.nowcoderdemo1.utils;

import com.sun.mail.smtp.DigestMD5;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

//This class is used for some frequent functions in the project
public class CommunityUtil {
    //generate random strings
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // use MD5 to encrypt passwords of users
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
