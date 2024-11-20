package com.tu.mall.service;

import cn.hutool.json.JSONObject;
import com.tu.mall.entity.Attribute;
import com.tu.mall.entity.AttributeValue;
import com.tu.mall.entity.view.AttributeView;

import java.util.List;

/**
 * <p>
 * 商品（基本/销售）属性表 服务类
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-10-10
 */
public interface IAttributeService {

    List<JSONObject> getAttributeAndValueList();

    void setAttribute(AttributeView attributeView);

    void delAttribute(AttributeView attributeView);

    List<Attribute> getAttribute(String categoryId);

    List<AttributeValue> getAttributeValue();

    List<AttributeValue> getValueByAttrId(String attrId);

    Attribute getAttrById(String attrId);

    AttributeValue getAttrValueById(String attrValueId);
}
