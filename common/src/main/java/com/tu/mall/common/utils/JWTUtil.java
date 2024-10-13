package com.tu.mall.common.utils;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.JWTSignerUtil;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 使用hutool的jwt工具
 *
 * @author JiFeiYe
 * @since 2024/10/11
 */
public class JWTUtil {

    private static final byte[] KEY = "liutiaorenxiangmushijian".getBytes(StandardCharsets.UTF_8);

    private static final long EXPIRE = 1000 * 60 * 60 * 24; // 24h

    /**
     * 创建jwt->token
     *
     * @param userId 用户id
     * @return {@code String}
     */
    public static String getToken(Long userId) {
        return JWT.create()
                .setSigner(JWTSignerUtil.hs256(KEY)) // 算法+密钥
                .setPayload("userId", userId) // 载荷
                .setExpiresAt(new Date(System.currentTimeMillis() + EXPIRE)) // 设置过期时间（1天）
                .sign();
    }

    /**
     * 校验jwt->token
     * 提取userId
     *
     * @param token token
     * @return {@code String}
     */
    public static String getUserId(String token) {
        JWT jwt = JWT.of(token);
        String userId = "";
        if (jwt.setKey(KEY).validate(0)) {
            userId = (String) jwt.getPayload("userId");
        }
        return userId;
    }
}
