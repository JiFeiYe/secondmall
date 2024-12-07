package com.tu.mall.controller;

import cn.hutool.json.JSONObject;
import com.tu.mall.common.result.Result;
import com.tu.mall.entity.Attribute;
import com.tu.mall.entity.AttributeValue;
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
    @GetMapping("/attrAndValue")
    public Result<List<JSONObject>> getAttributeAndValueList() {
        log.info("查询全部属性、属性值列表");

        List<JSONObject> jsonObjectList = attributeService.getAttributeAndValueList();
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
    public Result<String> setAttributeAndValue(@RequestBody AttributeView attributeView) {
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
    public Result<String> delAttributeAndValue(@RequestBody AttributeView attributeView) {
        log.info("删除属性、属性值，attributeView：{}", attributeView);

        attributeService.delAttribute(attributeView);
        return Result.ok();
    }

    /**
     * 通过二级分类id获取其下属分类对应属性
     *
     * @param categoryId 二级分类id
     * @return {@code Result<List<Attribute>>}
     */
    @ApiOperation("通过二级分类id获取其下属分类对应属性")
    @GetMapping("/attribute")
    public Result<List<Attribute>> getAttributeByCategoryId(String categoryId) {
        log.info("通过二级分类id获取其下属分类对应属性，categoryId：{}", categoryId);

        List<Attribute> attributeList = attributeService.getAttribute(categoryId);
        return Result.ok(attributeList);
    }

    /**
     * 根据属性获取其属性值
     *
     * @param attrId 属性id
     * @return {@code Result<List<AttributeValue>>}
     */
    @ApiOperation("根据属性获取其属性值")
    @GetMapping("/attrValueByAttr")
    public Result<List<AttributeValue>> getAttrValueByAttrId(String attrId) {
        log.info("根据属性获取其属性值");

        List<AttributeValue> attributeValueList = attributeService.getValueByAttrId(attrId);
        return Result.ok(attributeValueList);
    }

    /**
     * 获取全部属性值
     *
     * @return {@code Result<List<AttributeValue>>}
     */
    @ApiOperation("获取全部属性值")
    @GetMapping("/attrValue")
    public Result<List<AttributeValue>> getAttributeValue() {
        log.info("获取属性值");

        List<AttributeValue> attributeValueList = attributeService.getAttributeValue();
        return Result.ok(attributeValueList);
    }
}
