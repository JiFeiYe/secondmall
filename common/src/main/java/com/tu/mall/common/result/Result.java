package com.tu.mall.common.result;

import lombok.Data;

/**
 * @author JiFeiYe
 * @since 2024/9/4
 */
@Data
public class Result<T> {

    private Integer code; // 200
    private String message; // “success”
    private T data; // Object

    public Result() {
    }

    protected static <T> Result<T> build(T data, ResultCodeEnum resultCodeEnum) {
        Result<T> result = new Result<>();
        if (data != null) {
            result.setData(data);
        }
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    /**
     * 成功
     *
     * @param data 返回数据
     * @param <T>  泛型
     * @return {@code Result<T>}
     */
    public static <T> Result<T> ok(T data) {
        return build(data, ResultCodeEnum.SUCCESS); // 填充data与枚举
    }

    public static <T> Result<T> ok() {
        return Result.ok(null);
    }

    /**
     * 失败
     *
     * @param data 返回数据
     * @param <T>  泛型
     * @return {@code Result<T>}
     */
    public static <T> Result<T> fail(T data) {
        return build(data, ResultCodeEnum.FAIL);
    }

    public static <T> Result<T> fail() {
        return fail(null);
    }

    /**
     * 异常
     * <p>
     * code与message的内容已经在自定义异常中限制为ResultCodeEnum中出现的内容
     *
     * @param code    code
     * @param message message
     * @return {@code Result<T>}
     */
    public static <T> Result<T> exception(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> exception(ResultCodeEnum resultCodeEnum) {
        Result<T> result = new Result<>();
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }
}
