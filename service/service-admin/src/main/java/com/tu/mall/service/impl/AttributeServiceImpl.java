package com.tu.mall.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tu.mall.common.exception.CustomException;
import com.tu.mall.common.result.ResultCodeEnum;
import com.tu.mall.entity.Attribute;
import com.tu.mall.entity.AttributeValue;
import com.tu.mall.entity.view.AttributeView;
import com.tu.mall.mapper.AttributeMapper;
import com.tu.mall.mapper.AttributeValueMapper;
import com.tu.mall.mapper.view.AttributeViewMapper;
import com.tu.mall.service.IAttributeService;
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

    @Override
    public List<JSONObject> getAttributeList() {
        // 获取所有属性、属性值列表
        List<AttributeView> attributeViewList = attributeViewMapper.selectList(null);
        // 先按attrId分组
        Map<Long, List<AttributeView>> map = attributeViewList
                .stream()
                .filter(attributeView -> attributeView.getAttrId() != null) // 过滤null值
                .collect(Collectors.groupingBy(AttributeView::getAttrId));
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (Map.Entry<Long, List<AttributeView>> next : map.entrySet()) {
            JSONObject obj = JSONUtil.createObj();
            obj.set("attrId", next.getKey());
            obj.set("attrName", next.getValue().get(0).getAttrName());
            obj.set("categoryId", next.getValue().get(0).getCategoryId());
            List<JSONObject> jsonObjectList1 = new ArrayList<>();
            for (AttributeView attributeView : next.getValue()) {
                JSONObject obj1 = JSONUtil.createObj();
                obj1.set("attrValueId", attributeView.getAttrValueId());
                obj1.set("attrValueName", attributeView.getAttrValueName());
                jsonObjectList1.add(obj1);
            }
            obj.set("attrValue", jsonObjectList1);
            jsonObjectList.add(obj);
        }
        return jsonObjectList;
    }

    @Override
    @Transactional
    public void setAttribute(AttributeView attributeView) {
        Attribute attribute = new Attribute()
                .setName(attributeView.getAttrName())
                .setCategoryId(attributeView.getCategoryId());
        AttributeValue attributeValue = new AttributeValue()
                .setName(attributeView.getAttrValueName());

        if (ObjectUtil.isNotNull(attribute.getName())
                && ObjectUtil.isNotNull(attributeValue.getName())) {
            attributeMapper.insertOrUpdate(attribute);
            attributeValue.setAttrId(attribute.getId());
            attributeValueMapper.insertOrUpdate(attributeValue);
        } else if (ObjectUtil.isNotNull(attribute.getName())) {
            attributeMapper.insertOrUpdate(attribute);
        } else {
            throw new CustomException(ResultCodeEnum.ATTRIBUTE_FAIL);
        }
    }

    @Override
    public void delAttribute(AttributeView attributeView) {
        Long attrId = attributeView.getAttrId();
        Long attrValueId = attributeView.getAttrValueId();
        if (ObjectUtil.isNotNull(attrId) && ObjectUtil.isNotNull(attrValueId)) {
            attributeValueMapper.deleteById(attrValueId);
        } else if (ObjectUtil.isNotNull(attrId)) {
            attributeMapper.deleteById(attrId);
            LambdaQueryWrapper<AttributeValue> lqw = new LambdaQueryWrapper<>();
            lqw.eq(AttributeValue::getAttrId, attrId);
            attributeValueMapper.delete(lqw);
        } else {
            throw new CustomException(ResultCodeEnum.ATTRIBUTE_FAIL);
        }
    }
}
