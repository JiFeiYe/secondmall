package com.tu.mall.common.result;

import lombok.Getter;

/**
 * @author JiFeiYe
 * @since 2024/9/4
 */
@Getter
public enum ResultCodeEnum {
    SUCCESS(200, "success"),
    FAIL(400, "fail"),

    // 手动Result返回
    LOGIN_AUTH(4001, "尚未登录"),
    LOGIN_FAIL(4002, "登录失败"),
    CODE_FAIL(4003, "验证码无效"),
    ACCOUNT_EXIST(4004, "账户已存在"),

    // 自定义异常CustomException返回
    EMAIL_FAIL(5001, "邮件发送失败"),
    ;

    private final Integer code;
    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
