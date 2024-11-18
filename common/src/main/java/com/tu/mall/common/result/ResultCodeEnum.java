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

    FORBIDDEN(4000, "禁止访问"),
    LOGIN_AUTH(4001, "尚未登录"),
    LOGIN_FAIL(4002, "登录失败"),
    CODE_FAIL(4003, "验证码无效"),
    ACCOUNT_EXIST(4004, "账户已存在"),
    UPLOAD_FAIL(4005, "文件上传失败"),
    CATEGORY_FAIL(4006, "分类参数错误"),
    ATTRIBUTE_FAIL(4007, "属性参数错误"),
    SUBMIT_ORDER_FAIL(4008, "不能重复提交订单"),
    SEARCH_FAIL(4009, "搜索失败"),

    EMAIL_FAIL(5001, "邮件发送失败"),
    // 5002 IOException,
    ;

    private final Integer code;
    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
