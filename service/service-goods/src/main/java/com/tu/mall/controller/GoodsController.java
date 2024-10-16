package com.tu.mall.controller;

import com.tu.mall.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JiFeiYe
 * @since 2024/10/15
 */
@RestController
@RequestMapping // “/front/goods”被网关截断，不用写
@Slf4j
public class GoodsController {

    @PostMapping
    public Result<String> saveGoods() {
        return Result.ok();
    }

}
