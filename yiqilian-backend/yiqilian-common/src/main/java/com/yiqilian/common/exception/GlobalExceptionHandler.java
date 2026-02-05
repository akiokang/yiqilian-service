package com.yiqilian.common.exception;

import com.yiqilian.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 处理自定义业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        log.warn("业务警告: {}", e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    // 兜底处理：处理所有未知的运行时异常
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("系统运行崩溃: ", e); // 详细日志打在服务器
        return Result.fail(500, "服务器开小差了，请稍后再试");
    }
}