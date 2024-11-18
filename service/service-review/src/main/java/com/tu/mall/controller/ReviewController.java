package com.tu.mall.controller;

import com.tu.mall.common.result.Result;
import com.tu.mall.common.utils.AuthContextHolder;
import com.tu.mall.entity.AppReview;
import com.tu.mall.entity.OrderReview;
import com.tu.mall.service.IAppReviewService;
import com.tu.mall.service.IOrderReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author JiFeiYe
 * @since 2024/10/23
 */
@Api(tags = {"ReviewController"})
@RestController
@RequestMapping // "/front/review"
@Slf4j
public class ReviewController {

    @Autowired
    private IOrderReviewService orderReviewService;
    @Autowired
    private IAppReviewService appReviewService;

    /**
     * 设置订单评价
     *
     * @param orderReview 订单评价表
     * @return {@code Result<String>}
     */
    @ApiOperation("设置订单评价")
    @PostMapping("/order")
    public Result<String> setOrderReview(HttpServletRequest request, @RequestBody OrderReview orderReview) {
        log.info("设置订单评价，orderReview：{}", orderReview);

        orderReview.setUserId(AuthContextHolder.getUserId(request));
        orderReviewService.setOrderReview(orderReview);
        return Result.ok();
    }

    /**
     * 设置平台评价
     *
     * @param appReview 平台评价表
     * @return {@code Result<String>}
     */
    @ApiOperation("设置平台评价")
    @PostMapping("/app")
    public Result<String> setAppReview(HttpServletRequest request, @RequestBody AppReview appReview) {
        log.info("设置平台评价，appReview：{}", appReview);

        appReview.setUserId(AuthContextHolder.getUserId(request));
        appReviewService.setAppReview(appReview);
        return Result.ok();
    }

    /**
     * 获取订单评价
     *
     * @param orderId 订单id
     * @return {@code Result<OrderReview>}
     */
    @ApiOperation("获取订单评价")
    @GetMapping("/order")
    public Result<OrderReview> getOrderReview(String orderId) {
        log.info("获取订单评价");

        OrderReview orderReview = orderReviewService.getOrderReview(orderId);
        return Result.ok(orderReview);
    }
}
