package com.tu.mall.service;

import cn.hutool.json.JSONObject;
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

    List<JSONObject> getAttributeList();

    void setAttribute(AttributeView attributeView);

    void delAttribute(AttributeView attributeView);
}
