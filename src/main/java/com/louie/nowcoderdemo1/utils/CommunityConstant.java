package com.louie.nowcoderdemo1.utils;

public interface CommunityConstant {
    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;

    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE = 1;

    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 2;

    /**
     * 默认登录的超时时间
     */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    /**
     * 记住状态下登录超时时间
     */
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;
}
