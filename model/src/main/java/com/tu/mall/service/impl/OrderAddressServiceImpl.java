package com.tu.mall.service.impl;

import com.tu.mall.service.IOrderAddressService;
import com.tu.mall.entity.OrderAddress;
import com.tu.mall.mapper.OrderAddressMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 记录订单地址 服务实现类
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-10-10
 */
@Service
public class OrderAddressServiceImpl extends ServiceImpl<OrderAddressMapper, OrderAddress> implements IOrderAddressService {

}
