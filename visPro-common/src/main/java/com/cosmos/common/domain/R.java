package com.cosmos.common.domain;


import com.cosmos.common.common.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {
    private int code;   //响应码
    private String msg; //响应信息
    private T data;    //响应数据
    private String description; //响应描述

    //请求成功无数据

    /**
     * 返回成功的信息，无数据
     *
     * @param msg 请求成功的信息，无数据
     * @return 返回成功
     */
    public static <T>  R<T> success(String msg) {
        return new R<T>(200, msg, null, "");
    }


    /**
     * 请求成功有数据
     *
     * @param msg  请求成功的信息
     * @param data 请求成功的数据
     * @return 返回成功
     */
    //请求成功有数据
    public static <T> R<T> success(String msg, T data) {
        return new R<T>(200, msg, data, "");
    }


    //定义两个请求失败，一个直接传入通用的枚举类型的错误

    /**
     * 用于返回枚举类型通用错误
     *
     * @param errorCode 枚举类型通用错误
     * @return 返回错误
     */
    public static <T> R<T> error(ErrorCode errorCode) {
        return new <T>R<T>(errorCode.getCode(), errorCode.getMsg(), null, "");
    }


    //一个传入自定义的错误信息

    /**
     * 用于返回自定义的错误信息
     *
     * @param msg 自定义错误信息
     * @return 返回错误
     */
    public static <T> R<T> error(String msg) {
        return new R<T>(500, msg, null, "");
    }


    /**
     * 自定义错误信息和描述
     *
     * @param errorCode
     * @param description
     * @return
     */
    public static <T> R<T> error(ErrorCode errorCode, String description) {
        return new R<T>(errorCode.getCode(), errorCode.getMsg(), null, description);
    }

    public static <T> R<T> error(int code, String msg, String description) {
        return new R<T>(code, msg, null, description);
    }


}
