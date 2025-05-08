package com.tu.mall.controller;

import com.tu.mall.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JiFeiYe
 * @since 2024/10/30
 */
@RestController
@RequestMapping
@Slf4j
public class ChatController {

    @GetMapping
    public Result<String> test() {
        log.info("测试模型");

        return Result.ok();
    }
}
