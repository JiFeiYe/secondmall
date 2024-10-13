package com.tu.mall.common.exception;

import com.tu.mall.common.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常捕获
 *
 * @author JiFeiYe
 * @since 2024/10/11
 */
@RestControllerAdvice // 结合了RequestBody和ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 意料之外的异常
     *
     * @param e Exception
     * @return {@code Result<String>}
     */
    @ExceptionHandler(Exception.class)
    public Result<String> error(Exception e) {
        e.printStackTrace();
        return Result.fail();
    }

    /**
     * 自定义异常
     *
     * @param e CustomException
     * @return {@code Result<String>}
     */
    @ExceptionHandler(CustomException.class)
    public Result<String> error(CustomException e) {
        return Result.exception(e.getErrCode(), e.getMessage());
    }
}
