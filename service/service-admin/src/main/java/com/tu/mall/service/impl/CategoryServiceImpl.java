package com.tu.mall.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tu.mall.common.exception.CustomException;
import com.tu.mall.common.result.ResultCodeEnum;
import com.tu.mall.entity.Category1;
import com.tu.mall.entity.Category2;
import com.tu.mall.entity.Category3;
import com.tu.mall.entity.view.CategoryView;
import com.tu.mall.mapper.Category1Mapper;
import com.tu.mall.mapper.Category2Mapper;
import com.tu.mall.mapper.Category3Mapper;
import com.tu.mall.mapper.view.CategoryViewMapper;
import com.tu.mall.service.ICategoryService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author JiFeiYe
 * @since 2024/10/19
 */
@DubboService
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryViewMapper categoryViewMapper;
    @Autowired
    private Category1Mapper category1Mapper;
    @Autowired
    private Category2Mapper category2Mapper;
    @Autowired
    private Category3Mapper category3Mapper;

    @Override
    public List<JSONObject> getList() {
        // 获取所有分类列表
        List<CategoryView> categoryViewList = categoryViewMapper.selectList(null);
        List<JSONObject> jsonObjectList = new ArrayList<>();
        // 先按category1Id分组
        Map<Long, List<CategoryView>> categoryMap1 = // 键：category1Id，值：每个categoryViewList（按组分）
                categoryViewList.stream()
                        .filter(categoryView -> categoryView.getCategory1Id() != null) // 过滤null值
                        .collect(Collectors.groupingBy(CategoryView::getCategory1Id));
        for (Map.Entry<Long, List<CategoryView>> next1 : categoryMap1.entrySet()) {
            JSONObject jsonObject1 = JSONUtil.createObj();
            jsonObject1.set("category1Id", next1.getKey());
            jsonObject1.set("category1Name", next1.getValue().get(0).getCategory1Name());
            List<JSONObject> jsonObjectList1 = new ArrayList<>();
            List<CategoryView> categoryViewList1 = next1.getValue(); // 这个categoryViewList1的所有category1Id均相同
            Map<Long, List<CategoryView>> categoryMap2 = // 键：category2Id，值：每个categoryViewList1（按组分）
                    categoryViewList1.stream()
                            .filter(categoryView -> categoryView.getCategory2Id() != null) // 过滤null值
                            .collect(Collectors.groupingBy(CategoryView::getCategory2Id));
            for (Map.Entry<Long, List<CategoryView>> next2 : categoryMap2.entrySet()) {
                JSONObject jsonObject2 = JSONUtil.createObj();
                jsonObject2.set("category2Id", next2.getKey());
                jsonObject2.set("category2Name", next2.getValue().get(0).getCategory2Name());
                List<JSONObject> jsonObjectList2 = new ArrayList<>();
                List<CategoryView> categoryViewList2 = next2.getValue(); // 这个categoryViewList2的所有category2Id均相同
                Map<Long, List<CategoryView>> categoryMap3 = // 键：category3Id，值：每个categoryViewList2（按组分）
                        categoryViewList2.stream()
                                .filter(categoryView -> categoryView.getCategory3Id() != null) // 过滤null值
                                .collect(Collectors.groupingBy(CategoryView::getCategory3Id));
                for (Map.Entry<Long, List<CategoryView>> next3 : categoryMap3.entrySet()) {
                    JSONObject jsonObject3 = JSONUtil.createObj();
                    jsonObject3.set("category3Id", next3.getKey());
                    jsonObject3.set("category3Name", next3.getValue().get(0).getCategory3Name());
                    jsonObjectList2.add(jsonObject3);
                }
                jsonObject2.set("categoryChild", jsonObjectList2);
                jsonObjectList1.add(jsonObject2);
            }
            jsonObject1.set("categoryChild", jsonObjectList1);
            jsonObjectList.add(jsonObject1);
        }
        return jsonObjectList;
    }

    @Override
    @Transactional
    public void setCategory(CategoryView categoryView) {
        Category1 category1 = new Category1()
                .setId(categoryView.getCategory1Id())
                .setName(categoryView.getCategory1Name());
        Category2 category2 = new Category2()
                .setId(categoryView.getCategory2Id())
                .setName(categoryView.getCategory2Name());
        Category3 category3 = new Category3()
                .setId(categoryView.getCategory3Id())
                .setName(categoryView.getCategory3Name());

        if (ObjectUtil.isNotNull(category1.getName())
                && ObjectUtil.isNotNull(category2.getName())
                && ObjectUtil.isNotNull(category3.getName())) { // 给了一二三级分类信息
            category1Mapper.insertOrUpdate(category1);
            category2.setCategory1Id(category1.getId());
            category2Mapper.insertOrUpdate(category2);
            category3.setCategory2Id(category2.getId());
            category3Mapper.insertOrUpdate(category3);
        } else if (ObjectUtil.isNotNull(category1.getName())
                && ObjectUtil.isNotNull(category2.getName())) { // 给了一二级分类信息
            category1Mapper.insertOrUpdate(category1);
            category2.setCategory1Id(category1.getId());
            category2Mapper.insertOrUpdate(category2);
        } else if (ObjectUtil.isNotNull(category1.getName())) { // 只给了一级分了信息
            category1Mapper.insertOrUpdate(category1);
        } else {
            throw new CustomException(ResultCodeEnum.CATEGORY_FAIL);
        }
    }

    @Override
    public List<Category1> getCategory1() {
        return category1Mapper.selectList(null);
    }

    @Override
    public List<Category2> getCategory2(Long category1Id) {
        LambdaQueryWrapper<Category2> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Category2::getCategory1Id, category1Id);
        return category2Mapper.selectList(lqw);
    }

    @Override
    public List<Category3> getCategory3(Long category2Id) {
        LambdaQueryWrapper<Category3> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Category3::getCategory2Id, category2Id);
        return category3Mapper.selectList(lqw);
    }

    @Override
    @Transactional
    public void delCategory(CategoryView categoryView) {
        Long category1Id = categoryView.getCategory1Id();
        Long category2Id = categoryView.getCategory2Id();
        Long category3Id = categoryView.getCategory3Id();
        if (ObjectUtil.isNotNull(category1Id)
                && ObjectUtil.isNotNull(category2Id)
                && ObjectUtil.isNotNull(category3Id)) {
            category3Mapper.deleteById(category3Id);
        } else if (ObjectUtil.isNotNull(category1Id)
                && ObjectUtil.isNotNull(category2Id)) {
            category2Mapper.deleteById(category2Id);
            LambdaQueryWrapper<Category3> lqw = new LambdaQueryWrapper<>();
            lqw.eq(Category3::getCategory2Id, category2Id);
            category3Mapper.delete(lqw);
        } else if (ObjectUtil.isNotNull(category1Id)) {
            category1Mapper.deleteById(category1Id);
            LambdaQueryWrapper<Category2> lqw1 = new LambdaQueryWrapper<>();
            lqw1.eq(Category2::getCategory1Id, category1Id);
            category2Mapper.delete(lqw1);
            LambdaQueryWrapper<Category3> lqw2 = new LambdaQueryWrapper<>();
            lqw2.eq(Category3::getCategory2Id, category2Id);
            category3Mapper.delete(lqw2);
        } else {
            throw new CustomException(ResultCodeEnum.CATEGORY_FAIL);
        }
    }
}
