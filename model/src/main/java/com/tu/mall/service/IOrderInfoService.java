package com.tu.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tu.mall.entity.OrderInfo;

import java.util.Map;

/**
 * <p>
 * 订单详情信息 服务类
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-10-10
 */
public interface IOrderInfoService {

    Map<String, Object> getBeforeOrder(String userId, String skuId);

    OrderInfo submitOrder(OrderInfo orderInfo);

    Page<OrderInfo> getOrderByPage(int i, String userId, Integer page, Integer size);
}
