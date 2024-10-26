package com.tu.mall.controller;

import cn.hutool.json.JSONObject;
import com.tu.mall.common.result.Result;
import com.tu.mall.entity.view.AttributeView;
import com.tu.mall.service.IAttributeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author JiFeiYe
 * @since 2024/10/23
 */
@Api(tags = {"AttributeController"})
@RestController
@RequestMapping("/back/admin")
@Slf4j
public class AttributeController {

    @Autowired
    private IAttributeService attributeService;

    /**
     * 查询全部属性、属性值列表
     *
     * @return {@code Result<List<JSONObject>>}
     */
    @ApiOperation("查询全部属性、属性值列表")
    @GetMapping("/attribute")
    public Result<List<JSONObject>> getAttributeList() {
        log.info("查询全部属性、属性值列表");

        List<JSONObject> jsonObjectList = attributeService.getAttributeList();
        return Result.ok(jsonObjectList);
    }

    /**
     * 新增or修改属性、属性值
     *
     * @param attributeView 全部属性集合视图
     * @return {@code Result<String>}
     */
    @ApiOperation("新增or修改属性、属性值")
    @PostMapping("/attribute")
    public Result<String> setAttribute(@RequestBody AttributeView attributeView) {
        log.info("新增or修改属性、属性值，attributeView：{}", attributeView);

        attributeService.setAttribute(attributeView);
        return Result.ok();
    }

    /**
     * 删除属性、属性值
     *
     * @param attributeView 全部属性集合视图
     * @return {@code Result<String>}
     */
    @ApiOperation("删除属性、属性值")
    @DeleteMapping("/attribute")
    public Result<String> delAttribute(@RequestBody AttributeView attributeView) {
        log.info("删除属性、属性值，attributeView：{}", attributeView);

        attributeService.delAttribute(attributeView);
        return Result.ok();
    }
}
