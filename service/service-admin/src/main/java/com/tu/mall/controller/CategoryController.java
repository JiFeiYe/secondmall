package com.tu.mall.controller;

import cn.hutool.json.JSONObject;
import com.tu.mall.common.result.Result;
import com.tu.mall.entity.Category1;
import com.tu.mall.entity.Category2;
import com.tu.mall.entity.Category3;
import com.tu.mall.entity.view.CategoryView;
import com.tu.mall.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author JiFeiYe
 * @since 2024/10/19
 */
@RestController
@RequestMapping("/back/admin")
@Slf4j
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    /**
     * 获取全部分类信息
     *
     * @return {@code Result<List<JSONObject>>}
     */
    @GetMapping("/categorylist")
    public Result<List<JSONObject>> getCategoryList() {
        log.info("获取全部分类信息");

        List<JSONObject> jsonObjectList = categoryService.getList();
        return Result.ok(jsonObjectList);
    }

    /**
     * 获取一级分类
     *
     * @return {@code Result<Category1>}
     */
    @GetMapping("/category1")
    public Result<List<Category1>> getCategory1() {
        log.info("获取一级分类信息");

        List<Category1> category1List = categoryService.getCategory1();
        return Result.ok(category1List);
    }

    /**
     * 获取二级分类
     *
     * @param category1Id 一级分类id
     * @return {@code Result<Category2>}
     */
    @GetMapping("/category2")
    public Result<List<Category2>> getCategory2(Long category1Id) {
        log.info("获取二级分类信息，category1Id：{}", category1Id);

        List<Category2> category2List = categoryService.getCategory2(category1Id);
        return Result.ok(category2List);
    }

    /**
     * 获取三级分类
     *
     * @param category2Id 二级分类id
     * @return {@code Result<Category3>}
     */
    @GetMapping("/category3")
    public Result<List<Category3>> getCategory3(Long category2Id) {
        log.info("获取三级分类信息，category2Id：{}", category2Id);

        List<Category3> category3List = categoryService.getCategory3(category2Id);
        return Result.ok(category3List);
    }

    /**
     * 新增分类信息
     * <p>
     * 可能是新增一二三级分类，也可能新增二三级分类，也可能新增三级分类
     *
     * @param categoryView 全部分类视图
     * @return {@code Result<String>}
     */
    @PostMapping("/category")
    public Result<String> saveCategory(@RequestBody CategoryView categoryView) {
        log.info("添加分类信息，categoryView：{}",categoryView);

        categoryService.setCategory(categoryView);
        return Result.ok();
    }

    @DeleteMapping("/category")
    public Result<String> delCategory(@RequestBody CategoryView categoryView) {
        log.info("删除分类信息，categoryView：{}",categoryView);

        categoryService.delCategory(categoryView);
        return Result.ok();
    }
}
