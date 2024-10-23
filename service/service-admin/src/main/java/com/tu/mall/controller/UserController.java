package com.tu.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tu.mall.common.result.Result;
import com.tu.mall.common.result.ResultCodeEnum;
import com.tu.mall.entity.UserInfo;
import com.tu.mall.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author JiFeiYe
 * @since 2024/10/18
 */
@RestController
@RequestMapping("/back/admin")
@Slf4j
public class UserController {

    @DubboReference
    private IUserInfoService userInfoService;

    /**
     * 管理员登录
     *
     * @param account  管理员账号
     * @param password 密码
     * @return {@code Result<Map<String, String>>}
     */
    @PostMapping("/login")
    public Result<Map<String, String>> login(String account, String password) {
        log.info("开始登录，account：{}，password：{}", account, password);

        Map<String, String> map = userInfoService.loginAdmin(account, password);
        if (map == null)
            return Result.exception(ResultCodeEnum.LOGIN_FAIL);
        return Result.ok(map);
    }

    /**
     * 分页查询所有用户or管理员列表
     *
     * @param page 当前页面
     * @param size 页面大小
     * @return {@code Result<Page<UserInfo>>}
     */
    @GetMapping("/user")
    public Result<Page<UserInfo>> getUserInfoByPage(Integer page, Integer size, Integer identity) {
        log.info("获取用户信息列表，page：{}，size：{}", page, size);

        return Result.ok(userInfoService.getUserInfoByPage(page, size, identity));
    }

    /**
     * 修改用户or管理员信息
     *
     * @param userInfo 用户信息
     * @return {@code Result<String>}
     */
    @PutMapping("/user")
    public Result<String> updateUserInfo(@RequestBody UserInfo userInfo) {
        log.info("修改用户/管理员信息，useInfo：{}", userInfo);

        userInfoService.updateUserInfo(userInfo);
        return Result.ok();
    }
}
