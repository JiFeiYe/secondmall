package com.tu.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tu.mall.entity.SkuInfo;

/**
 * <p>
 * sku主信息 服务类
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-10-10
 */
public interface ISkuInfoService {

    SkuInfo saveGoods(String userId, SkuInfo skuInfo);

    IPage<SkuInfo> getGoods(String userId, Integer page, Integer size);

    void updateGoods(String userId, SkuInfo skuInfo);

    void delGoods(Long skuId);
}
