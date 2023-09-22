package com.coder.community.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.coder.community.entity.LoginTicket;

@Mapper
@Deprecated
public interface LoginTicketMapper {
    
    @Insert({
        "INSERT INTO login_ticket(user_id,ticket,status,expired) ", 
        "VALUES(#{userId}, #{ticket}, #{status}, #{expired}) "
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    @Select({
        "SELECT id, user_id, ticket, status, expired ", 
        "FROM login_ticket WHERE ticket=#{ticket} "
    })
    LoginTicket selectByTicket(String ticket);

    @Update({
        "UPDATE login_ticket SET status=#{status} WHERE ticket=#{ticket} "
    })
    int updateStatus(String ticket, int status);

}
