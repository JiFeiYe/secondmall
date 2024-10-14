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

    LOGIN_AUTH(4001, "尚未登录"),
    LOGIN_FAIL(4002, "登录失败"),
    ;

    private final Integer code;
    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
