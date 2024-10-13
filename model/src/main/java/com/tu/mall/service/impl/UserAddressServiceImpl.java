package com.tu.mall.service.impl;

import com.tu.mall.service.IUserAddressService;
import com.tu.mall.entity.UserAddress;
import com.tu.mall.mapper.UserAddressMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户地址表 服务实现类
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-10-10
 */
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements IUserAddressService {

}
