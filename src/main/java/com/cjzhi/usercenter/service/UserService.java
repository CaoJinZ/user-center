package com.cjzhi.usercenter.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjzhi.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
* @author caojinzhi
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-04-01 15:59:50
*/
public interface UserService extends IService<User> {

    long userRegister(String userAccount, String userPassword, String checkPassword);

    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    User getSafetyUser(User user);

    /**
     * 用户注销
     */
    int userLogout(HttpServletRequest request);

}
