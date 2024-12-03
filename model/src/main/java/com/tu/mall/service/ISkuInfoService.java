package com.tu.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tu.mall.entity.SkuAttributeValue;
import com.tu.mall.entity.SkuInfo;

import java.util.List;

/**
 * <p>
 * sku主信息 服务类
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-10-10
 */
public interface ISkuInfoService {

    SkuInfo saveGoods(SkuInfo skuInfo);

    Page<SkuInfo> getGoods(String userId, Integer page, Integer size);

    Page<SkuInfo> getGoods(Integer page, Integer size);

    SkuInfo getGoods(String skuId);

    void updateGoods(String userId, SkuInfo skuInfo);

    void delGoods(String skuId);

    String getUserIdBySkuId(String skuId);

    List<SkuAttributeValue> getAttrBySkuId(String skuId);
}
