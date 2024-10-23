package com.tu.mall.controller;

import cn.hutool.json.JSONObject;
import com.tu.mall.common.result.Result;
import com.tu.mall.entity.view.AttributeView;
import com.tu.mall.service.IAttributeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author JiFeiYe
 * @since 2024/10/23
 */
@RestController
@RequestMapping("/back/admin")
@Slf4j
public class AttributeController {

    @Autowired
    private IAttributeService attributeService;

    @GetMapping("/attribute")
    public Result<List<JSONObject>> getAttributeList() {
        log.info("查询全部属性、属性值列表");

        List<JSONObject> jsonObjectList = attributeService.getAttributeList();
        return Result.ok(jsonObjectList);
    }

    @PostMapping("/attribute")
    public Result<String> setAttribute(@RequestBody AttributeView attributeView) {
        log.info("新增or修改属性、属性值，attributeView：{}", attributeView);

        attributeService.setAttribute(attributeView);
        return Result.ok();
    }

    @DeleteMapping("/attribute")
    public Result<String> delAttribute(@RequestBody AttributeView attributeView) {
        log.info("删除属性、属性值，attributeView：{}", attributeView);

        attributeService.delAttribute(attributeView);
        return Result.ok();
    }
}
