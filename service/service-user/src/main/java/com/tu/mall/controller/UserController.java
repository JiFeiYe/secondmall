package com.tu.mall.controller;

import com.tu.mall.common.result.Result;
import com.tu.mall.common.result.ResultCodeEnum;
import com.tu.mall.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author JiFeiYe
 * @since 2024/10/13
 */
@RestController
@RequestMapping("/api1/user")
@Slf4j
public class UserController {

    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 用户登录
     *
     * @param email    邮箱
     * @param password 密码
     * @return {@code Result<Map<String, String>>}
     */
    @PostMapping("/login")
    public Result<Map<String, String>> login(String email, String password) {
        log.info("开始登录，email：{}，password：{}", email, password);

        Map<String, String> map = userInfoService.login(email, password);
        if (map == null)
            return Result.exception(ResultCodeEnum.LOGIN_FAIL);
        return Result.ok(map);
    }
}
