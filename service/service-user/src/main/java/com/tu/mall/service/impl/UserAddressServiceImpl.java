package com.tu.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tu.mall.entity.UserAddress;
import com.tu.mall.mapper.UserAddressMapper;
import com.tu.mall.service.IUserAddressService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author JiFeiYe
 * @since 2024/10/14
 */
@DubboService
public class UserAddressServiceImpl implements IUserAddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    // 分页获取用户地址
    @Override
    public IPage<UserAddress> getUserAddress(String userId, Integer page, Integer size) {
        IPage<UserAddress> pages = new Page<>(page, size);
        LambdaQueryWrapper<UserAddress> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserAddress::getUserId, Long.valueOf(userId));
        return userAddressMapper.selectPage(pages, lqw);
    }

    // 新增用户地址
    @Override
    public void saveAddress(String userId, UserAddress userAddress) {
        userAddress.setUserId(Long.valueOf(userId));
        userAddressMapper.insert(userAddress);
    }

    // 更新用户地址
    @Override
    public void updateAddress(String userId, UserAddress userAddress) {
        userAddress.setUserId(Long.valueOf(userId));
        userAddressMapper.updateById(userAddress);
    }

    // 删除用户地址
    @Override
    public void delAddress(String userId, String userAddressId) {
        LambdaQueryWrapper<UserAddress> lqw = new LambdaQueryWrapper<UserAddress>()
                .eq(UserAddress::getUserId, Long.valueOf(userId))
                .eq(UserAddress::getId, Long.valueOf(userAddressId));
        userAddressMapper.delete(lqw);
    }
}
