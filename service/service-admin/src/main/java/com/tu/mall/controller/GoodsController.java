package com.tu.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tu.mall.api.SearchService;
import com.tu.mall.common.result.Result;
import com.tu.mall.entity.SkuInfo;
import com.tu.mall.service.ISkuInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

/**
 * @author JiFeiYe
 * @since 2024/10/23
 */
@Api(tags = {"GoodsController"})
@RestController
@RequestMapping("/back/admin")
@Slf4j
public class GoodsController {

    @DubboReference
    private SearchService searchService;
    @DubboReference
    private ISkuInfoService skuInfoService;

    /**
     * 查询所有商品
     *
     * @param page 当前页面
     * @param size 页面大小
     * @return {@code Result<Page<SkuInfo>>}
     */
    @ApiOperation("查询所有商品")
    @GetMapping("/goods")
    public Result<Page<SkuInfo>> getSkuInfoList(Integer page, Integer size) {
        log.info("查询所有商品，page：{}，size：{}", page, size);

        Page<SkuInfo> skuInfoPage = skuInfoService.getGoods(page, size);
        return Result.ok(skuInfoPage);
    }

    /**
     * 商品上架
     *
     * @param skuInfo 商品信息
     * @return {@code Result<String>}
     */
    @ApiOperation("商品上架")
    @PostMapping("/goods/upper")
    public Result<String> upperGoods(@RequestBody SkuInfo skuInfo) {
        log.info("商品上架，skuInfo：{}", skuInfo);

        searchService.upperGoods(skuInfo);
        return Result.ok();
    }

    /**
     * 商品下架
     *
     * @param skuId 商品id
     * @return {@code Result<String>}
     */
    @ApiOperation("商品下架")
    @PostMapping("/goods/lower")
    public Result<String> lowerGoods(String skuId) {
        log.info("商品下架，skuId：{}", skuId);

        searchService.lowerGoods(skuId);
        return Result.ok();
    }

    /**
     * 删除商品
     *
     * @param skuId 商品id
     * @return {@code Result<String>}
     */
    @ApiOperation("删除商品")
    @DeleteMapping("/goods")
    public Result<String> delGoods(String skuId) {
        log.info("删除商品，skuId：{}", skuId);

        searchService.lowerGoods(skuId);
        skuInfoService.delGoods(skuId);
        return Result.ok();
    }
}
