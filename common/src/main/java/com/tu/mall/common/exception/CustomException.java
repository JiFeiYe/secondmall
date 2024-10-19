package com.tu.mall.common.exception;

import com.tu.mall.common.result.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义异常
 *
 * @author JiFeiYe
 * @since 2024/10/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomException extends RuntimeException {

    // 异常状态码
    private Integer errCode;

    /**
     * 构造函数
     * 接收错误信息与错误码
     *
     * @param message 错误信息
     * @param errCode 错误码
     */
    public CustomException(String message, Integer errCode) {
        super(message);
        this.errCode = errCode;
    }

    /**
     * 构造函数
     * 接收枚举类型对象
     *
     * @param codeEnum 错误码枚举
     */
    public CustomException(ResultCodeEnum codeEnum) {
        super(codeEnum.getMessage());
        this.errCode = codeEnum.getCode();
    }

    @Override
    public String toString() {
        return "CustomException{" +
                "errCode=" + errCode +
                ", message=" + this.getMessage() +
                '}';
    }
}
