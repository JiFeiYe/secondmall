package com.tu.mall.api;

import com.tu.mall.entity.SkuInfo;

/**
 * @author JiFeiYe
 * @since 2024/10/17
 */
public interface SearchService {

    void upperGoods(SkuInfo skuInfo);

    void lowerGoods(Long goodId);
}
