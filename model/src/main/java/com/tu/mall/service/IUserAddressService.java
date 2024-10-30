package com.tu.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tu.mall.entity.UserAddress;

import java.util.List;

/**
 * <p>
 * 用户地址表 服务类
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-10-10
 */
public interface IUserAddressService {

    Page<UserAddress> getUserAddress(String userId, Integer page, Integer size);

    List<UserAddress> getUserAddress(String userId);

    void saveAddress(String userId, UserAddress userAddress);

    void updateAddress(String userId, UserAddress userAddress);

    void delAddress(String userId, String userAddressId);
}
