package com.tu.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tu.mall.common.utils.JWTUtil;
import com.tu.mall.entity.UserInfo;
import com.tu.mall.mapper.UserInfoMapper;
import com.tu.mall.service.IUserInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JiFeiYe
 * @since 2024/10/13
 */
@DubboService
public class UserInfoServiceImpl implements IUserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public Map<String, String> login(String email, String password) {
        // 验证账号存在
        LambdaQueryWrapper<UserInfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserInfo::getEmail, email);
        UserInfo userInfo = userInfoMapper.selectOne(lqw);
        if (userInfo == null) {
            return null;
        }
        // 验证密码
        String md5Pwd = userInfo.getPassword(); // 加密后的
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        System.out.println("md5Password:" + md5Password);
        if (!StrUtil.equals(md5Pwd, md5Password)) {
            return null;
        }
        // 生成JWT
        String token = JWTUtil.getToken(userInfo.getUserId());
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("name", userInfo.getName());
        return map;
    }
}
