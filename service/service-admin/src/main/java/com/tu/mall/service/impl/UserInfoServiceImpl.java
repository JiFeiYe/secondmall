package com.tu.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tu.mall.entity.UserInfo;
import com.tu.mall.mapper.UserInfoMapper;
import com.tu.mall.service.IUserInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Map;

/**
 * @author JiFeiYe
 * @since 2024/10/22
 */
@DubboService
public class UserInfoServiceImpl implements IUserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public Map<String, String> login(String email, String password) {
        return Collections.emptyMap();
    }

    @Override
    public void verifyEmail(String email) {

    }

    @Override
    public void generateCode(UserInfo userInfo) {

    }

    @Override
    public void setUserInfo(String userId, String password, String code) {

    }

    @Override
    public void updateUserInfo(UserInfo userInfo) {
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public Map<String, String> loginAdmin(String account, String password) {
        return Collections.emptyMap();
    }

    @Override
    public Page<UserInfo> getUserInfoByPage(Integer page, Integer size, Integer identity) {
        Page<UserInfo> userInfoPage = new Page<>(page, size);
        LambdaQueryWrapper<UserInfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserInfo::getIdentity, identity);
        return userInfoMapper.selectPage(userInfoPage, lqw);
    }
}
