package com.tu.mall.common.result;

import lombok.Getter;

/**
 * @author JiFeiYe
 * @since 2024/9/4
 */
@Getter
public enum ResultCodeEnum {
    SUCCESS(200, "success"),
    FAIL(201, "fail"),

    LOGIN_AUTH(202, "尚未登录"),
    ;

    private final Integer code;
    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
