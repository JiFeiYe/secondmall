package com.tu.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tu.mall.api.SearchService;
import com.tu.mall.common.result.Result;
import com.tu.mall.common.utils.AuthContextHolder;
import com.tu.mall.entity.SkuInfo;
import com.tu.mall.service.ISkuInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author JiFeiYe
 * @since 2024/10/15
 */
@Api(tags = {"GoodsController"})
@RestController
@RequestMapping // “/front/goods”被网关截断，不用写
@Slf4j
public class GoodsController {

    @Autowired
    private ISkuInfoService skuInfoService;

    @DubboReference
    private SearchService searchService;

    /**
     * 发布商品
     *
     * @param skuInfo 商品信息
     * @return {@code Result<String>}
     */
    @ApiOperation("发布商品")
    @PostMapping
    public Result<String> saveGoods(HttpServletRequest request, @RequestBody SkuInfo skuInfo) {
        log.info("保存商品进mysql，skuInfo：{}", skuInfo);

        String userId = AuthContextHolder.getUserId(request);
        skuInfo.setUserId(userId);
        SkuInfo skuInfo1 = skuInfoService.saveGoods(skuInfo); // 填充skuInfo.skuImgList返回
        // 调用es上架方法
        skuInfo1.setImgFileList(null);
        searchService.upperGoods(skuInfo1);
        return Result.ok();
    }

    /**
     * 分页查询当前用户已发布商品
     *
     * @param page 当前页面
     * @param size 页面大小
     * @return {@code Result<Page<SkuInfo>>}
     */
    @ApiOperation("分页查询当前用户已发布商品")
    @GetMapping
    public Result<Page<SkuInfo>> getGoods(HttpServletRequest request, Integer page, Integer size) {
        log.info("分页查询当前用户已发布商品，page：{}，size：{}", page, size);

        String userId = AuthContextHolder.getUserId(request);
        Page<SkuInfo> skuInfoPage = skuInfoService.getGoods(userId, page, size);
        return Result.ok(skuInfoPage);
    }

    /**
     * 更新商品信息
     *
     * @param skuInfo 商品信息
     * @return {@code Result<String>}
     */
    @ApiOperation("更新商品信息")
    @PutMapping
    public Result<String> updateGoods(HttpServletRequest request, @RequestBody SkuInfo skuInfo) {
        log.info("更新商品信息mysql，skuInfo：{}", skuInfo);

        String userId = AuthContextHolder.getUserId(request);
        skuInfoService.updateGoods(userId, skuInfo);
        // 调用es商品更新方法（下架后重新上架）
        skuInfo.setImgFileList(null);
        searchService.lowerGoods(skuInfo.getId());
        searchService.upperGoods(skuInfo);
        return Result.ok();
    }

    /**
     * 删除商品
     *
     * @param skuId 商品id
     * @return {@code Result<String>}
     */
    @ApiOperation("删除商品")
    @DeleteMapping
    public Result<String> delGoods(String skuId) {
        log.info("删除商品，skuId：{}", skuId);

        // 调用es下架商品方法
        searchService.lowerGoods(skuId);
        skuInfoService.delGoods(skuId);
        return Result.ok();
    }
}
