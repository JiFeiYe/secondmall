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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 搜索相关接口，此服务不对外
 *
 * @author JiFeiYe
 * @since 2024/10/17
 */
@Api(tags = {"搜索相关接口，此服务不对外"})
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
    public Result<String> lowerGoods(Long goodId) {
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
}
