package com.louie.nowcoderdemo1.dao;

import com.louie.nowcoderdemo1.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface LoginTicketMapper {
    int insertLoginTicket(LoginTicket loginTicket);

    LoginTicket getLoginTicket(String ticket);

    int updateLoginTicket(String ticket, int status);
}
