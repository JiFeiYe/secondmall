package com.tu.mall.common.utils;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.JWTSignerUtil;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author JiFeiYe
 * @since 2024/10/11
 */
public class TestJWT {

    private static final byte[] KEY = "liutiaorenxiangmushijian".getBytes(StandardCharsets.UTF_8);

    private static final long EXPIRE = 1000 * 60 * 60 * 24; // 24h

    @Test
    public void getToken() {
        String token = JWT.create()
                .setSigner(JWTSignerUtil.hs256(KEY)) // 算法+密钥
                .setPayload("userId", 1L) // 载荷
                .setExpiresAt(new Date(System.currentTimeMillis() + EXPIRE)) // 设置过期时间（1天）
                .sign();
        System.out.println(token);
    }

    @Test
    public void verify() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImV4cCI6MTcyODcyMjkzMX0.ocQinU5w-DiYFCXTMX3yAZCMEvY0_axykn8KkstQ7mY";
        JWT jwt = JWT.of(token);
        boolean b = jwt.setKey(KEY).validate(0);
        if (b) {
            System.out.println(jwt.getHeader());
            System.out.println(jwt.getPayloads());
            System.out.println(jwt.getPayload("userId"));
        }
    }
}
