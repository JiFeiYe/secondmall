package com.tu.mall.service.impl;

import com.tu.mall.service.IOrderItemService;
import com.tu.mall.entity.OrderItem;
import com.tu.mall.mapper.OrderItemMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单商品信息（时效） 服务实现类
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-10-10
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements IOrderItemService {

}
