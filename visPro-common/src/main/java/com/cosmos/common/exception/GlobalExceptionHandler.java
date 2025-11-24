package com.cosmos.common.exception;


import com.cosmos.common.common.ErrorCode;
import com.cosmos.common.domain.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局异常拦截器，处理未被controller层吸收的异常
 */


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    //自定义异常拦截器;从异常中获取异常信息利用统一接口返回
    @ExceptionHandler(BusinessException.class)
    public R<?> businessExceptionHandler(BusinessException e) {
        //打印日志
        log.error("businessException:Code:{} Message:{} Description{}", e.getCode(), e.getMessage(), e.getDescription());
        return R.error(e.getCode(), e.getMsg(), e.getDescription());
    }


    //运行异常拦截器
    @ExceptionHandler(RuntimeException.class)
    public R<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return R.error(ErrorCode.SYSTEM_ERROR);
    }


}
