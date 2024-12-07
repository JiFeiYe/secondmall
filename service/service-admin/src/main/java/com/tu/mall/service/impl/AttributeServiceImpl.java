package com.tu.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tu.mall.common.exception.CustomException;
import com.tu.mall.common.result.ResultCodeEnum;
import com.tu.mall.entity.Attribute;
import com.tu.mall.entity.AttributeValue;
import com.tu.mall.entity.Category2;
import com.tu.mall.entity.view.AttributeView;
import com.tu.mall.mapper.AttributeMapper;
import com.tu.mall.mapper.AttributeValueMapper;
import com.tu.mall.mapper.view.AttributeViewMapper;
import com.tu.mall.service.IAttributeService;
import com.tu.mall.service.ICategoryService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author JiFeiYe
 * @since 2024/10/23
 */
@DubboService
public class AttributeServiceImpl implements IAttributeService {

    @Autowired
    private AttributeViewMapper attributeViewMapper;
    @Autowired
    private AttributeMapper attributeMapper;
    @Autowired
    private AttributeValueMapper attributeValueMapper;
    @DubboReference
    private ICategoryService categoryService;

    @Override
    public List<JSONObject> getAttributeAndValueList() { // 包含第二级分类清单,三级太多太繁杂了
        // 获取所有属性、属性值列表
        List<AttributeView> attributeViewList = attributeViewMapper.selectList(null);
        // 先按attrId分组
        Map<String, List<AttributeView>> map = attributeViewList
                .stream()
                .filter(attributeView -> attributeView.getAttrId() != null) // 过滤null值
                .collect(Collectors.groupingBy(AttributeView::getAttrId));
        List<JSONObject> jsonObjectList = new ArrayList<>();
        List<Category2> category2List = categoryService.getCategory2("");
        for (Category2 category2 : category2List) {
            JSONObject categoryObj = JSONUtil.createObj();
            categoryObj.set("categoryId", category2.getId());
            categoryObj.set("categoryName", category2.getName());
            categoryObj.set("label", category2.getName());
            List<JSONObject> jsonObjectList1 = new ArrayList<>();
            for (Map.Entry<String, List<AttributeView>> next : map.entrySet()) {
                if (StrUtil.equals(next.getValue().get(0).getCategoryId(), category2.getId())) {
                    JSONObject obj = JSONUtil.createObj();
                    obj.set("attrId", next.getKey());
                    obj.set("attrName", next.getValue().get(0).getAttrName());
                    obj.set("label", next.getValue().get(0).getAttrName());
                    obj.set("categoryId", next.getValue().get(0).getCategoryId());
                    List<JSONObject> jsonObjectList2 = new ArrayList<>();
                    for (AttributeView attributeView : next.getValue()) {
                        JSONObject obj1 = JSONUtil.createObj();
                        obj1.set("attrValueId", attributeView.getAttrValueId());
                        obj1.set("attrValueName", attributeView.getAttrValueName());
                        obj1.set("label", attributeView.getAttrValueName());
                        jsonObjectList2.add(obj1);
                    }
                    obj.set("children", jsonObjectList2);
                    jsonObjectList1.add(obj);
                }
            }
            categoryObj.set("children", jsonObjectList1);
            jsonObjectList.add(categoryObj);
        }
        return jsonObjectList;
    }

    @Override
    @Transactional
    public void setAttribute(AttributeView attributeView) {
        Attribute attribute = new Attribute()
                .setId(attributeView.getAttrId())
                .setName(attributeView.getAttrName())
                .setCategoryId(attributeView.getCategoryId());
        AttributeValue attributeValue = new AttributeValue()
                .setId(attributeView.getAttrValueId())
                .setName(attributeView.getAttrValueName())
                .setAttrId(attributeView.getAttrId());
        if (StrUtil.isNotEmpty(attribute.getName())
                && StrUtil.isNotEmpty(attributeValue.getName())) { // （新增、更新）属性and属性值
            attributeMapper.insertOrUpdate(attribute);
            attributeValue.setAttrId(attribute.getId());
            attributeValueMapper.insertOrUpdate(attributeValue);
        } else if (StrUtil.isNotEmpty(attribute.getName())) { // （新增、更新）属性
            attributeMapper.insertOrUpdate(attribute);
        } else {
            throw new CustomException(ResultCodeEnum.ATTRIBUTE_FAIL);
        }
    }

    @Override
    public void delAttribute(AttributeView attributeView) {
        String attrId = attributeView.getAttrId();
        String attrValueId = attributeView.getAttrValueId();
        if (StrUtil.isNotEmpty(attrId) && StrUtil.isNotEmpty(attrValueId)) { // 删除某一属性值
            attributeValueMapper.deleteById(attrValueId);
        } else if (StrUtil.isNotEmpty(attrId)) { // 删除属性及其所有属性值
            attributeMapper.deleteById(attrId);
            LambdaQueryWrapper<AttributeValue> lqw = new LambdaQueryWrapper<>();
            lqw.eq(AttributeValue::getAttrId, attrId);
            attributeValueMapper.delete(lqw);
        } else {
            throw new CustomException(ResultCodeEnum.ATTRIBUTE_FAIL);
        }
    }

    @Override
    public List<Attribute> getAttribute(String categoryId) {
        LambdaQueryWrapper<Attribute> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Attribute::getCategoryId, categoryId);
        List<Attribute> attributeList = attributeMapper.selectList(lqw);
        if (StrUtil.isEmpty(categoryId)) {
            for (Attribute attribute : attributeList) {
                Category2 category2 = categoryService.getCategory2ById(attribute.getCategoryId());
                attribute.setCategoryName(category2.getName());
            }
        }
        return attributeList;
    }

    @Override
    public List<AttributeValue> getAttributeValue() {
        return attributeValueMapper.selectList(null);
    }

    @Override
    public List<AttributeValue> getValueByAttrId(String attrId) {
        LambdaQueryWrapper<AttributeValue> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AttributeValue::getAttrId, attrId);
        return attributeValueMapper.selectList(lqw);
    }

    @Override
    public Attribute getAttrById(String attrId) {
        return attributeMapper.selectById(attrId);
    }

    @Override
    public AttributeValue getAttrValueById(String attrValueId) {
        return attributeValueMapper.selectById(attrValueId);
    }
}
