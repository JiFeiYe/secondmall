package com.tu.mall.service;

import cn.hutool.json.JSONObject;
import com.tu.mall.entity.Category1;
import com.tu.mall.entity.Category2;
import com.tu.mall.entity.Category3;
import com.tu.mall.entity.view.CategoryView;

import java.util.List;

/**
 * <p>
 * 一级分类 服务类
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-10-10
 */
public interface ICategoryService {

    List<JSONObject> getList();

    void setCategory(CategoryView categoryView);

    List<Category1> getCategory1();

    List<Category2> getCategory2(String category1Id);

    List<Category3> getCategory3(String category1Id);

    void delCategory(CategoryView categoryView);
}
