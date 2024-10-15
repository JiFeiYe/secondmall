package com.tu.mall.common.utils;

import cn.hutool.core.util.StrUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author JiFeiYe
 * @since 2024/10/14
 */
public class AuthContextHolder {

    public static String getUserId(HttpServletRequest request) {
        String userId = request.getHeader("userId");
        return StrUtil.isEmpty(userId) ? "" : userId;
    }
}
