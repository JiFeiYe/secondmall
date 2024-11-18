package com.tu.mall.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tu.mall.common.result.Result;
import com.tu.mall.common.result.ResultCodeEnum;
import com.tu.mall.common.utils.AuthContextHolder;
import com.tu.mall.common.utils.JWTUtil;
import com.tu.mall.entity.UserAddress;
import com.tu.mall.entity.UserInfo;
import com.tu.mall.service.IUserAddressService;
import com.tu.mall.service.IUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JiFeiYe
 * @since 2024/10/13
 */
@Api(tags = {"UserController"})
@RestController
@RequestMapping // "/front/user"被网关截断，无须再写
@Slf4j
public class UserController {

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IUserAddressService userAddressService;

    /**
     * 用户登录
     *
     * @param email    用户邮箱
     * @param password 密码
     * @return {@code Result<Map<String, String>>}
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<Map<String, String>> login(String email, String password) {
        log.info("开始登录，email：{}，password：{}", email, password);

        Map<String, String> map = userInfoService.login(email, password);
        if (map == null)
            return Result.exception(ResultCodeEnum.LOGIN_FAIL);
        return Result.ok(map);
    }

    /**
     * 生成邮箱验证码并发送给用户
     * <p>
     * 验证码限时五分钟
     * <p>
     * 需验证邮箱未注册
     *
     * @param email 用户邮箱
     * @return {@code Result<Map<String, String>>}
     */
    @ApiOperation("生成邮箱验证码并发送给用户")
    @GetMapping("/register/code")
    public Result<Map<String, String>> getRegisterCode(String email) {
        log.info("获取邮箱验证码，email：{}", email);

        if (StrUtil.isEmpty(email))
            return Result.fail();
        // 验证邮箱未注册
        userInfoService.verifyEmail(email);
        // 生成验证码，发送邮件
        UserInfo userInfo = new UserInfo();
        String userId = String.valueOf(IdWorker.getId(userInfo)); // todo 非原子操作？是否导致id不再单调递增？是否会有id冲突？
        userInfo.setUserId(userId)
                .setEmail(email)
                .setName("默认用户_" + StrUtil.sub(userId, 0, 14));
        userInfoService.generateCode(userInfo);

        // 生成JWT
        String token = JWTUtil.getToken(userInfo.getUserId());
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("name", userInfo.getName());
        return Result.ok(map);
    }

    /**
     * 同上一个方法，但不用验证邮箱未注册
     *
     * @return {@code Result<String>}
     */
    @ApiOperation("生成邮箱验证码并发送给用户")
    @GetMapping("/recover/code")
    public Result<String> getRecoverCode(HttpServletRequest request) {
        log.info("获取邮箱验证码");

        // 生成验证码，发送邮件
        String userId = AuthContextHolder.getUserId(request);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfoService.generateCode(userInfo);

        return Result.ok();
    }

    /**
     * 校验验证码，记录用户注册、更新密码信息
     *
     * @param password 用户密码
     * @param code     验证码
     * @return {@code Result<String>}
     */
    @ApiOperation("校验验证码，记录用户注册、更新密码信息")
    @PostMapping("/verify")
    public Result<String> setUserInfo(HttpServletRequest request, String password, String code) {
        log.info("校验验证码，记录信息，code：{}", code);

        String userId = AuthContextHolder.getUserId(request);
        userInfoService.setUserInfo(userId, password, code);
        return Result.ok();
    }

    /**
     * 修改个人信息
     *
     * @param userInfo 个人信息详情
     * @return {@code Result<String>}
     */
    @ApiOperation("修改个人信息")
    @PutMapping("/info")
    public Result<String> updateUserInfo(HttpServletRequest request, @RequestBody UserInfo userInfo) {
        log.info("修改个人信息，userInfo：{}", userInfo);

        String userId = AuthContextHolder.getUserId(request);
        userInfo.setUserId(userId);
        userInfoService.updateUserInfo(userInfo);
        return Result.ok();
    }

    /**
     * 分页获取用户地址列表
     *
     * @param page 当前页面
     * @param size 页面大小
     * @return {@code Result<Page<UserAddress>>}
     */
    @ApiOperation("分页获取用户地址列表")
    @GetMapping("/address")
    public Result<Page<UserAddress>> getAddress(HttpServletRequest request, Integer page, Integer size) {
        log.info("分页获取用户地址列表，page：{}，size：{}", page, size);

        String userId = AuthContextHolder.getUserId(request);
        Page<UserAddress> addressPage = userAddressService.getUserAddress(userId, page, size);
        return Result.ok(addressPage);
    }

    /**
     * 新增用户地址
     *
     * @param userAddress 用户地址详情
     * @return {@code Result<String>}
     */
    @ApiOperation("新增用户地址")
    @PostMapping("/address")
    public Result<String> saveAddress(HttpServletRequest request, @RequestBody UserAddress userAddress) {
        log.info("新增用户地址，userAddress：{}", userAddress);

        String userId = AuthContextHolder.getUserId(request);
        userAddressService.saveAddress(userId, userAddress);
        return Result.ok();
    }

    /**
     * 更新用户地址
     *
     * @param userAddress 用户地址详情
     * @return {@code Result<String>}
     */
    @ApiOperation("更新用户地址")
    @PutMapping("/address")
    public Result<String> updateAddress(HttpServletRequest request, @RequestBody UserAddress userAddress) {
        log.info("更新用户地址，userAddress：{}", userAddress);

        String userId = AuthContextHolder.getUserId(request);
        userAddressService.updateAddress(userId, userAddress);
        return Result.ok();
    }

    /**
     * 删除用户地址
     *
     * @param userAddressId 用户地址id
     * @return {@code Result<String>}
     */
    @ApiOperation("删除用户地址")
    @DeleteMapping("/address")
    public Result<String> delAddress(HttpServletRequest request, String userAddressId) {
        log.info("删除用户地址，userAddressId：{}", userAddressId);

        String userId = AuthContextHolder.getUserId(request);
        userAddressService.delAddress(userId, userAddressId);
        return Result.ok();
    }


}
