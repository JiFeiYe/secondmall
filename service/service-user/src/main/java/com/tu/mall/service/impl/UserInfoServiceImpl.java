package com.tu.mall.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tu.mall.common.exception.CustomException;
import com.tu.mall.common.result.ResultCodeEnum;
import com.tu.mall.common.utils.JWTUtil;
import com.tu.mall.entity.UserInfo;
import com.tu.mall.mapper.UserInfoMapper;
import com.tu.mall.service.IUserInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author JiFeiYe
 * @since 2024/10/13
 */
@DubboService
public class UserInfoServiceImpl implements IUserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

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

    // 验证账号存在
    @Override
    public void verifyEmail(String email) {
        LambdaQueryWrapper<UserInfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserInfo::getEmail, email);
        UserInfo one = userInfoMapper.selectOne(lqw);
        if (ObjectUtil.isNotNull(one)) {
            throw new CustomException(ResultCodeEnum.ACCOUNT_EXIST);
        }
    }

    @Override
    public void generateCode(String userId, String email) {
        // 生成验证码
        SecureRandom random = new SecureRandom();
        int i = random.nextInt(1000000); // 生成一个0到999999之间的随机数
        String code = String.format("%06d", i); // 将数字转换为六位数的字符串

        // 保存进redis
        String key = "Verify_Email_Code_" + userId;
        HashMap<Object, Object> value = MapUtil.of(new String[][]{
                {"email", email},
                {"code", code}
        });
        String jsonValue = JSONUtil.toJsonStr(value);
        redisTemplate.opsForValue().set(key, jsonValue, 5, TimeUnit.MINUTES); // // 过期时间五分钟

        // 发送邮件
//        mailService.sendHTMLMail(email, code);
    }

    // 新增、更新用户信息
    @Override
    public void setUserInfo(String userId, String password, String code) {
        String key = "Verify_Email_Code_" + userId;
        String redisMap = redisTemplate.opsForValue().getAndDelete(key);
        JSONObject jsonObject = JSONUtil.parseObj(redisMap);
        String email = jsonObject.getStr("email");
        String redisCode = jsonObject.getStr("code");
        if (!StrUtil.equals(redisCode, code)) { // 验证码无效
            throw new CustomException(ResultCodeEnum.CODE_FAIL);
        }
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        UserInfo userInfo = new UserInfo()
                .setUserId(Long.valueOf(userId))
                .setEmail(email)
                .setPassword(password);
        userInfoMapper.insertOrUpdate(userInfo);
    }

    @Override
    public void updateUserInfo(String userId, UserInfo userInfo) {
        userInfo.setUserId(Long.valueOf(userId));
        userInfoMapper.updateById(userInfo);
    }

}
