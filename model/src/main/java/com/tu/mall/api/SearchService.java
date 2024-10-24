package com.tu.mall.api;

import com.tu.mall.entity.SkuInfo;
import com.tu.mall.entity.es.SearchParam;
import com.tu.mall.entity.es.SearchResponseVo;

import java.io.IOException;

/**
 * @author JiFeiYe
 * @since 2024/10/17
 */
public interface SearchService {

    void upperGoods(SkuInfo skuInfo);

    void lowerGoods(Long goodId);

    SearchResponseVo search(SearchParam searchParam);
}
