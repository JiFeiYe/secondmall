package com.tu.mall.service;

import com.tu.mall.entity.OrderReview;

/**
 * <p>
 * 订单评价 服务类
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-10-30
 */
public interface IOrderReviewService {

    void setOrderReview(OrderReview orderReview);

    OrderReview getOrderReview(String orderId);
}
