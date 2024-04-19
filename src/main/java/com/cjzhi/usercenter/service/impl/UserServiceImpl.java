package com.cjzhi.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjzhi.usercenter.model.domain.User;
import com.cjzhi.usercenter.service.UserService;
import com.cjzhi.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cjzhi.usercenter.constant.UserConstant.USER_LOGIN_STATE;


/**
* @author caojinzhi
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-04-01 15:59:50
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    UserMapper userMapper;
    private static final String sALT = "cjzhi";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1 校验
        if (StringUtils.isAnyBlank(userAccount,userPassword, checkPassword)){
            //TODO 修改为自定义异常
            return -1;
        }
        if (userAccount.length() < 4){
            return -1;
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }
        //账号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1;
        }
        //账号不能包含特殊字符
        String regex = "^[a-zA-Z0-9_]{5,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userAccount);
        if (matcher.matches()) {
            return -1;
        }
        //密码和校验密码相同
        if(!userPassword.equals(checkPassword)){
            return -1;
        }
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(((sALT + userPassword).getBytes()));
        //插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(md5DigestAsHex);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1;
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1 校验
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        if (userAccount.length() < 4){
            return null;
        }
        if (userPassword.length() < 8) {
            return null;
        }
        //账号不能包含特殊字符
        String regex = "^[a-zA-Z0-9_]{5,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userAccount);
        if (!matcher.matches()) {
            return null;
        }

        //账号密码比对
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(((sALT + userPassword).getBytes()));
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("user_password", md5DigestAsHex);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("账号或密码错误");
            return null;
        }

        //用户脱敏
        User safetyUser = getSafetyUser(user);
        //记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    @Override
    public User getSafetyUser(User user) {
        if (user == null){
            return null;
        }
        //用户脱敏
        User safeUser = new User();
        safeUser.setId(user.getId());
        safeUser.setUserName(user.getUserName());
        safeUser.setUserAccount(user.getUserAccount());
        safeUser.setAvatarUrl(user.getAvatarUrl());
        safeUser.setGender(user.getGender());
        safeUser.setPhone(user.getPhone());
        safeUser.setEmail(user.getEmail());
        safeUser.setUserStatus(user.getUserStatus());
        safeUser.setCreateTime(user.getCreateTime());

        return safeUser;
    }

    //注销用户
    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }


}




