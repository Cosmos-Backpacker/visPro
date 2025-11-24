package com.cosmos.common.exception;


import com.cosmos.common.common.ErrorCode;
import lombok.Getter;

/**
 * @author CosmosBackpacker
 */
@Getter

public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */

    private int code;
    /**
     * 错误信息
     */
    private final String msg;

    /**
     * 错误描述
     */
    private String description;


    public BusinessException(int code, String msg, String description) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.description = description;
    }

    public BusinessException(String msg) {
        super(msg);
        this.msg = msg;
    }


    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMsg());  //将errorCode定义好的message传入
        this.msg = errorCode.getMsg();
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }


    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMsg());  //将errorCode定义好的message传入
        this.msg = errorCode.getMsg();
        this.code = errorCode.getCode();
        this.description = description;
    }


}
