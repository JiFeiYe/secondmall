package com.tu.mall.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tu.mall.api.MyApi;
import com.tu.mall.entity.UserInfo;
import com.tu.mall.mapper.UserInfoMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author JiFeiYe
 * @since 2024/10/11
 */
@DubboService
public class MyApiImpl implements MyApi {

    @Autowired
    private UserInfoMapper userInfoMapper;

    public List<UserInfo> getUserInfoList() {
        LambdaQueryWrapper<UserInfo> lqw = new LambdaQueryWrapper<>();
        return userInfoMapper.selectList(lqw);
    }
}
