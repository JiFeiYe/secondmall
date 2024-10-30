package com.tu.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tu.mall.common.result.Result;
import com.tu.mall.common.utils.AuthContextHolder;
import com.tu.mall.entity.OrderInfo;
import com.tu.mall.service.IOrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * @author JiFeiYe
 * @since 2024/10/28
 */
@Api(tags = {"OrderController"})
@RestController
@RequestMapping
@Slf4j
public class OrderController {

    @Autowired
    private IOrderInfoService orderInfoService;

    /**
     * 获取预备下订单时的页面信息
     *
     * @param skuId 商品id
     * @return {@code Result<Map<String, Object>>}
     */
    @ApiOperation("获取预备下订单时的页面信息")
    @PostMapping("/beforeOrder")
    public Result<Map<String, Object>> beforeOrder(HttpServletRequest request, Long skuId) {
        log.info("获取预备下订单时的页面信息，skuId：{}", skuId);

        String userId = AuthContextHolder.getUserId(request);
        Map<String, Object> map = orderInfoService.getBeforeOrder(userId, skuId);
        return Result.ok(map);
    }

    /**
     * 提交订单
     *
     * @param orderInfo 订单信息
     * @return {@code Result<String>}
     */
    @ApiOperation("提交订单")
    @PostMapping
    public Result<String> submitOrder(@RequestBody OrderInfo orderInfo) {
        log.info("提交订单，orderInfo：{}", orderInfo);

        orderInfoService.submitOrder(orderInfo);
        return Result.ok();
    }

    /**
     * 获取用户所有购买订单信息
     *
     * @param page 当前页面
     * @param size 页面大小
     * @return {@code Result<Page<OrderInfo>>}
     */
    @ApiOperation("获取用户所有购买订单信息")
    @GetMapping("/buyerorder")
    public Result<Page<OrderInfo>> getBuyerOrderByPage(HttpServletRequest request, Integer page, Integer size) {
        log.info("获取用户所有购买订单信息，page：{}，size：{}", page, size);

        String userId = AuthContextHolder.getUserId(request);
        Page<OrderInfo> orderByPage = orderInfoService.getOrderByPage(1, userId, page, size); // 1我买到的
        return Result.ok(orderByPage);
    }

    /**
     * 获取用户所有出售订单信息
     *
     * @param page 当前页面
     * @param size 页面大小
     * @return {@code Result<Page<OrderInfo>>}
     */
    @ApiOperation("获取用户所有出售订单信息")
    @GetMapping("/sellerorder")
    public Result<Page<OrderInfo>> getsellerOrderByPage(HttpServletRequest request, Integer page, Integer size) {
        log.info("获取用户所有出售订单信息，page：{}，size：{}", page, size);

        String userId = AuthContextHolder.getUserId(request);
        Page<OrderInfo> orderByPage = orderInfoService.getOrderByPage(0, userId, page, size); // 0我卖出的
        return Result.ok(orderByPage);
    }
}
