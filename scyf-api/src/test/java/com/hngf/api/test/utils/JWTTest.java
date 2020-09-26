package com.hngf.api.test.utils;

import com.hngf.api.common.utils.JwtUtil;
import com.hngf.entity.sys.User;
import io.jsonwebtoken.Claims;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class JWTTest {

   /* @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void getToken(){

        User user = new User();
        user.setUserId(1L);
        user.setLoginName("admin");
        String token = jwtUtil.generateToken(user);
        System.out.println(token);
    }

    @Test
    public void checkToken(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzY3lmIiwiaWQiOjEsIm5hbWUiOiJhZG1pbiIsImlhdCI6MTU5MjIxMzc0MywiZXhwIjoxNTkyODE4NTQzfQ.OaC0nBtu2lnFkWFArVvVqljByejK1CQz9vF0CjzWhXQ";
        Claims claims = jwtUtil.checkJWT(token);
        if(claims != null){
            String name = (String)claims.get("name");
            int id =(Integer) claims.get("id");
            System.out.println(name);
            System.out.println(id);
        }else{
            System.out.println("非法token");
        }
    }*/
}
