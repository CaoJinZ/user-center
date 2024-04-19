package com.cjzhi.usercenter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserCenterApplicationTests {

    @Test
    void contextLoads() {
        String s = "12345";
        int n = s.length();
        System.out.println(n);
    }

}
