package com.tu.mall.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tu.mall.common.exception.CustomException;
import com.tu.mall.common.result.ResultCodeEnum;
import com.tu.mall.common.utils.JWTUtil;
import com.tu.mall.entity.UserInfo;
import com.tu.mall.mapper.UserInfoMapper;
import com.tu.mall.service.IUserInfoService;
import com.tu.mall.template.OSSTemplate;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
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
    private OSSTemplate ossTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Map<String, String> login(String email, String password) {
        // 验证账号存在
        LambdaQueryWrapper<UserInfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserInfo::getEmail, email).select(UserInfo.class, i -> true); // 查询全部字段
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
    public void generateCode(UserInfo userInfo) {
        // 已存在的账号，补全账号信息
        UserInfo u = userInfoMapper.selectById(userInfo.getUserId());
        if (ObjectUtil.isNotNull(u)) {
            userInfo = u;
        }

        // 生成验证码
        SecureRandom random = new SecureRandom();
        int i = random.nextInt(1000000); // 生成一个0到999999之间的随机数
        String code = String.format("%06d", i); // 将数字转换为六位数的字符串

        // 保存进redis
        String key = "Verify_Email_Code_" + userInfo.getUserId().toString();
        HashMap<Object, Object> value = MapUtil.of(new String[][]{
                {"name", userInfo.getName()},
                {"email", userInfo.getEmail()},
                {"code", code}
        });
        String jsonValue = JSONUtil.toJsonStr(value);
        redisTemplate.opsForValue().set(key, jsonValue, 5, TimeUnit.MINUTES); // 过期时间五分钟

        // 发送邮件
//        mailService.sendHTMLMail(email, code);
    }

    // 新增、更新用户信息
    @Override
    public void setUserInfo(String userId, String password, String code) {
        // 从redis获取信息
        String key = "Verify_Email_Code_" + userId;
        String redisMap = redisTemplate.opsForValue().get(key);
        JSONObject jsonObject = JSONUtil.parseObj(redisMap);
        String name = jsonObject.getStr("name");
        String email = jsonObject.getStr("email");
        String redisCode = jsonObject.getStr("code");
        // 验证验证码
        if (!StrUtil.equals(redisCode, code)) { // 验证码无效
            throw new CustomException(ResultCodeEnum.CODE_FAIL);
        }
        // 加密
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        // 设置用户信息
        UserInfo userInfo = new UserInfo()
                .setUserId(userId)
                .setName(name)
                .setEmail(email)
                .setPassword(password);
        userInfoMapper.insertOrUpdate(userInfo);

        redisTemplate.delete(key);
    }

    @Override
    @Transactional
    public void updateUserInfo(UserInfo userInfo) {
        if (userInfo.getPictureFile() != null && !userInfo.getPictureFile().isEmpty()) {
            String url = ossTemplate.upload(userInfo.getPictureFile());
            userInfo.setPicture(url);
        }
        if (StrUtil.isNotEmpty(userInfo.getAccount())) {
            userInfo.setIdentity(1);
        } else {
            userInfo.setIdentity(0);
        }
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public Map<String, String> loginAdmin(String account, String password) {
        // 验证账号存在
        LambdaQueryWrapper<UserInfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserInfo::getAccount, account).select(UserInfo.class, i -> true); // 查询所有字段
        UserInfo userInfo = userInfoMapper.selectOne(lqw);
        if (ObjectUtil.isNull(userInfo)) {
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

    @Override
    public Page<UserInfo> getUserInfoByPage(Integer page, Integer size, Integer identity) {
        Page<UserInfo> userInfoPage = new Page<>(page, size);
        LambdaQueryWrapper<UserInfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserInfo::getIdentity, identity);
        return userInfoMapper.selectPage(userInfoPage, lqw);
    }

    @Override
    public UserInfo getUserInfo(String userId) {
        return userInfoMapper.selectById(userId);
    }

}
