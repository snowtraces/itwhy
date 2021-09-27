package com.snowtraces.itwhy.dto.common;

/**
 * 响应结果枚举
 *
 * @author CHENG
 */
public enum ApiResultEnum {
    SUCCESS(1, "请求成功"),
    FAILURE(0, "请求失败"),
    EXCEPTION(-1, "请求失败"),

    ;

    ApiResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 响应码
     */
    public Integer code;

    /**
     * 响应消息
     */
    public String msg;
}
