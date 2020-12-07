package com.louie.nowcoderdemo1.config;

import com.louie.nowcoderdemo1.controller.interceptor.LoginTicketInterceptor;
import com.louie.nowcoderdemo1.controller.interceptor.NeedLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Autowired
    private NeedLoginInterceptor needLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/css/*.css", "/js/*.js", "/img/*.png", "/img/*.jpg", "/img/*.jpeg");

        registry.addInterceptor(needLoginInterceptor)
                .excludePathPatterns("/css/*.css", "/js/*.js", "/img/*.png", "/img/*.jpg", "/img/*.jpeg");

    }
}
