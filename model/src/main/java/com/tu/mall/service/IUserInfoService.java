package com.tu.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tu.mall.entity.UserInfo;

import java.util.Map;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-10-10
 */
public interface IUserInfoService {

    Map<String, String> login(String email, String password);

    void verifyEmail(String email);

    void generateCode(UserInfo userInfo);

    void setUserInfo(String userId, String password, String code);

    void updateUserInfo(UserInfo userInfo);

    Map<String, String> loginAdmin(String account, String password);

    Page<UserInfo> getUserInfoByPage(Integer page, Integer size, Integer identity);

    UserInfo getUserInfo(String userId);
}
