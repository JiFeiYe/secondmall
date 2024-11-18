package com.tu.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tu.mall.entity.OrderReview;
import com.tu.mall.mapper.OrderReviewMapper;
import com.tu.mall.service.IOrderReviewService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author JiFeiYe
 * @since 2024/10/30
 */
@DubboService
public class OrderReviewServiceImpl implements IOrderReviewService {

    @Autowired
    private OrderReviewMapper orderReviewMapper;

    @Override
    public void setOrderReview(OrderReview orderReview) {
        orderReviewMapper.insert(orderReview);
    }

    @Override
    public OrderReview getOrderReview(String orderId) {
        LambdaQueryWrapper<OrderReview> lqw = new LambdaQueryWrapper<>();
        lqw.eq(OrderReview::getOrderId, orderId);
        return orderReviewMapper.selectOne(lqw);
    }
}
