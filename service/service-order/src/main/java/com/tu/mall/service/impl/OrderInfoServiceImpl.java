package com.tu.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tu.mall.api.SearchService;
import com.tu.mall.common.exception.CustomException;
import com.tu.mall.common.result.ResultCodeEnum;
import com.tu.mall.entity.*;
import com.tu.mall.mapper.OrderAddressMapper;
import com.tu.mall.mapper.OrderInfoMapper;
import com.tu.mall.mapper.OrderItemMapper;
import com.tu.mall.service.IOrderInfoService;
import com.tu.mall.service.ISkuInfoService;
import com.tu.mall.service.IUserAddressService;
import com.tu.mall.service.IUserInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author JiFeiYe
 * @since 2024/10/29
 */
@DubboService
public class OrderInfoServiceImpl implements IOrderInfoService {

    @DubboReference
    private IUserInfoService userInfoService;
    @DubboReference
    private IUserAddressService userAddressService;
    @DubboReference
    private ISkuInfoService skuInfoService;
    @DubboReference
    private SearchService searchService;

    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderAddressMapper orderAddressMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public Map<String, Object> getBeforeOrder(String userId, String skuId) {
        UserInfo userInfo = userInfoService.getUserInfo(userId);
        List<UserAddress> userAddressList = userAddressService.getUserAddress(userId);
        SkuInfo skuInfo = skuInfoService.getGoods(skuId);

        for (UserAddress userAddress : userAddressList) {
            userAddress.setName(userInfo.getName());
            userAddress.setPhone(userInfo.getPhone());
        }

        Map<String, Object> map = new HashMap<>();
        if (ObjectUtil.isNotNull(userInfo)) {
            map.put("userInfo", userInfo);
        }
        if (CollUtil.isNotEmpty(userAddressList)) {
            map.put("userAddressList", userAddressList);
        }
        if (ObjectUtil.isNotNull(skuInfo)) {
            map.put("skuInfo", skuInfo);
        }
        return map;
    }

    @Override
    @Transactional
    public OrderInfo submitOrder(OrderInfo orderInfo) {
        SkuInfo skuInfo = orderInfo.getSkuInfo();
        OrderAddress orderAddress1 = orderInfo.getOrderAddress();
//        List<OrderAddress> orderAddressList = new ArrayList<>();
        if (ObjectUtil.isNotNull(orderAddress1)) { // 收货人地址
            orderAddress1.setType(1); // 1收货人
            orderAddress1.setDeliveryWay("自提");
            orderAddress1.setFreight(new BigDecimal(0));
            orderAddress1.setOrderId(IdWorker.getIdStr(orderInfo));
//            orderAddressList.add(orderAddress1);
        } else
            throw new CustomException(ResultCodeEnum.SUBMIT_ORDER_FAIL);

        String sellerId = orderInfo.getSellerId();
        if (StrUtil.isEmpty(sellerId)) {
            sellerId = skuInfoService.getUserIdBySkuId(skuInfo.getId()); // 获取卖家id
            if (StrUtil.isNotEmpty(sellerId)) { // 发货人地址
//                OrderAddress orderAddress = new OrderAddress();
//                UserInfo userInfo = userInfoService.getUserInfo(sellerId);
//                List<UserAddress> userAddress1 = userAddressService.getUserAddress(sellerId);
//                BeanUtil.copyProperties(userInfo, orderAddress);
//                BeanUtil.copyProperties(userAddress1.get(0), orderAddress);
//                orderAddress.setType(0); // 0发货人
//                orderAddress.setDeliveryWay("自提");
//                orderAddress.setFreight(new BigDecimal(0));
//                orderAddressList.add(orderAddress);
                orderInfo.setSellerId(sellerId);
            } else
                throw new CustomException(ResultCodeEnum.SUBMIT_ORDER_FAIL);
        } else {
//            OrderAddress orderAddress = new OrderAddress();
//            UserInfo userInfo = userInfoService.getUserInfo(sellerId);
//            List<UserAddress> userAddress1 = userAddressService.getUserAddress(sellerId);
//            BeanUtil.copyProperties(userInfo, orderAddress);
//            BeanUtil.copyProperties(userAddress1, orderAddress);
//            orderAddress.setType(0); // 0发货人
//            orderAddress.setDeliveryWay("自提");
//            orderAddress.setFreight(new BigDecimal(0));
//            orderAddressList.add(orderAddress);
            orderInfo.setSellerId(sellerId);
        }

        orderAddressMapper.insert(orderAddress1);
        int insert = orderInfoMapper.insert(orderInfo);
        if (insert == 0) {
            throw new CustomException(ResultCodeEnum.SUBMIT_ORDER_FAIL);
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(orderInfo.getId());
        orderItem.setSkuId(orderInfo.getSkuInfo().getId());
        String skuAttrValueId;
        List<SkuAttributeValue> skuAttributeValueList = skuInfoService.getAttrBySkuId(orderItem.getSkuId());
        if (CollUtil.isNotEmpty(skuAttributeValueList)) {
            List<String> ids = skuAttributeValueList.stream().map(SkuAttributeValue::getId).collect(Collectors.toList());
            skuAttrValueId = String.join(",", ids);
            orderItem.setSkuAttrValueId(skuAttrValueId);
        }
        orderItem.setPrice(skuInfo.getPrice());
        orderItemMapper.insert(orderItem);

        return orderInfo;
    }

    @Override
    public Page<OrderInfo> getOrderByPage(int i, String userId, Integer page, Integer size) {
        Page<OrderInfo> orderInfoPage = new Page<>(page, size);
        if (i == 1) { // 购买订单（我买到的）
            LambdaQueryWrapper<OrderInfo> lqw = new LambdaQueryWrapper<>();
            lqw.eq(OrderInfo::getBuyerId, userId);
            orderInfoPage = orderInfoMapper.selectPage(orderInfoPage, lqw);

        } else if (i == 0) { // 卖出订单（我卖出的）
            LambdaQueryWrapper<OrderInfo> lqw = new LambdaQueryWrapper<>();
            lqw.eq(OrderInfo::getSellerId, userId);
            orderInfoPage = orderInfoMapper.selectPage(orderInfoPage, lqw);
        }
        List<OrderInfo> orderInfoList = new ArrayList<>();
        for (OrderInfo orderInfo : orderInfoPage.getRecords()) {
            String orderId = orderInfo.getId();
            {
                LambdaQueryWrapper<OrderAddress> lqw1 = new LambdaQueryWrapper<>();
                lqw1.eq(OrderAddress::getOrderId, orderId).eq(OrderAddress::getType, 1); // 只给收货地址
                OrderAddress orderAddress = orderAddressMapper.selectOne(lqw1);
                orderInfo.setOrderAddress(orderAddress);
            }
            {
                LambdaQueryWrapper<OrderItem> lqw1 = new LambdaQueryWrapper<>();
                lqw1.eq(OrderItem::getOrderId, orderId);
                OrderItem orderItem = orderItemMapper.selectOne(lqw1);
                orderInfo.setOrderItem(orderItem);
            }
            {
                String skuId = orderInfo.getOrderItem().getSkuId();
                SkuInfo skuInfo = skuInfoService.getGoods(skuId);
                skuInfo.setPrice(orderInfo.getOrderItem().getPrice());
                orderInfo.setSkuInfo(skuInfo);
            }
            orderInfoList.add(orderInfo);
        }
        orderInfoPage.setRecords(orderInfoList);
        return orderInfoPage;
    }

    @Override
    public void payOrder(String orderId, String payPrice) {
        LambdaUpdateWrapper<OrderInfo> luw = new LambdaUpdateWrapper<>();
        luw.eq(OrderInfo::getId, orderId)
                .set(OrderInfo::getPayPrice, payPrice).set(OrderInfo::getStatus, 1);
        orderInfoMapper.update(luw);

        LambdaQueryWrapper<OrderItem> lqw = new LambdaQueryWrapper<>();
        lqw.eq(OrderItem::getOrderId, orderId);
        OrderItem orderItem = orderItemMapper.selectOne(lqw);
        searchService.lowerGoods(orderItem.getSkuId());
    }
}
