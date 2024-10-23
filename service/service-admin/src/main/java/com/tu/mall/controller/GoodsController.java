package com.tu.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tu.mall.api.SearchService;
import com.tu.mall.common.result.Result;
import com.tu.mall.entity.SkuInfo;
import com.tu.mall.service.ISkuInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

/**
 * @author JiFeiYe
 * @since 2024/10/23
 */
@RestController
@RequestMapping("/back/admin")
@Slf4j
public class GoodsController {

    @DubboReference
    private SearchService searchService;
    @DubboReference
    private ISkuInfoService skuInfoService;

    @GetMapping("/goods")
    public Result<Page<SkuInfo>> getSkuInfoList(Integer page, Integer size) {
        log.info("查询所有商品，page：{}，size：{}", page, size);

        Page<SkuInfo> skuInfoPage = skuInfoService.getGoods(page, size);
        return Result.ok(skuInfoPage);
    }

    @PostMapping("/goods/upper")
    public Result<String> upperGoods(@RequestBody SkuInfo skuInfo) {
        log.info("商品上架，skuInfo：{}", skuInfo);

        searchService.upperGoods(skuInfo);
        return Result.ok();
    }

    @PostMapping("/goods/lower")
    public Result<String> lowerGoods(Long skuId) {
        log.info("商品下架，skuId：{}", skuId);

        searchService.lowerGoods(skuId);
        return Result.ok();
    }

    @DeleteMapping("/goods")
    public Result<String> delGoods(Long skuId) {
        log.info("删除商品，skuId：{}", skuId);

        skuInfoService.delGoods(skuId);
        return Result.ok();
    }
}
