package com.cjzhi.usercenter.service;
import com.cjzhi.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UserServiceTest {

    @Resource
    UserService userService;
    @Test
    void testSaveUser(){
        User user = new User();
        user.setUserName("Tom");
        user.setUserAccount("123");
        user.setAvatarUrl("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw1%2F2b7fed7b-e4a5-4298-8566-bd8e39783d2e%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1714551392&t=62bee67f1509b59137c91e4360ce41cd");
        user.setUserPassword("1234");
        user.setPhone("5632");
        user.setEmail("5555");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);

    }

    @Test
    void userRegister() {
        String userAccount = "cjzhi1";
        String userPassword = "123456789";
        String checkPassword = "123456789";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertTrue(result > 0);
    }
}