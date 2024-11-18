package com.tu.mall.controller;

import com.tu.mall.api.SearchService;
import com.tu.mall.common.result.Result;
import com.tu.mall.entity.SkuInfo;
import com.tu.mall.entity.es.SearchParam;
import com.tu.mall.entity.es.SearchResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 搜索相关接口
 *
 * @author JiFeiYe
 * @since 2024/10/17
 */
@Api(tags = {"SearchController"})
@RestController
@RequestMapping // "/front/search"被网关截断，免写
@Slf4j
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * 上架商品
     *
     * @param skuInfo 商品信息
     * @return {@code Result<String>}
     */
    @ApiOperation("上架商品")
    @PostMapping("/upper")
    public Result<String> upperGoods(@RequestBody SkuInfo skuInfo) {
        log.info("开始上架商品， skuInfo：{}", skuInfo);

        searchService.upperGoods(skuInfo);
        return Result.ok();
    }

    /**
     * 下架商品
     *
     * @param goodId 商品id
     * @return {@code Result<String>}
     */
    @ApiOperation("下架商品")
    @PostMapping("/lower")
    public Result<String> lowerGoods(String goodId) {
        log.info("开始下架商品， goodId：{}", goodId);

        searchService.lowerGoods(goodId);
        return Result.ok();
    }

    /**
     * 搜索
     *
     * @param searchParam 搜索参数
     * @return {@code Result<SearchResponse>}
     */
    @ApiOperation("搜索")
    @PostMapping
    public Result<SearchResponseVo> search(@RequestBody SearchParam searchParam) {
        log.info("搜索商品，searchParam：{}", searchParam);

        SearchResponseVo searchResponseVo = searchService.search(searchParam);
        return Result.ok(searchResponseVo);
    }

    /**
     * 搜索单个商品
     *
     * @param skuId 商品id
     * @return {@code Result<SearchResponseVo>}
     */
    @ApiOperation("搜索单个商品")
    @GetMapping
    public Result<SearchResponseVo> searchById(String skuId) {
        log.info("获取一个商品，skuId：{}", skuId);

        SearchResponseVo vo = searchService.search(skuId);
        return Result.ok(vo);
    }
}
