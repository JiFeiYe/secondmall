package com.tu.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tu.mall.entity.UserAddress;

/**
 * <p>
 * 用户地址表 服务类
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-10-10
 */
public interface IUserAddressService {

    IPage<UserAddress> getUserAddress(String userId, Integer page, Integer size);

    void saveAddress(String userId, UserAddress userAddress);

    void updateAddress(String userId, UserAddress userAddress);

    void delAddress(String userId, String userAddressId);
}
