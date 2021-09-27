package com.snowtraces.itwhy.dto.common;

import java.io.Serializable;

/**
 * API统一返回数据结构封装
 *
 * @author CHENG
 */
public class ApiResult<T> implements Serializable {

    private String msg;

    private int code;

    private T data;

    public ApiResult() {

    }

    public boolean isSuccess() {
        return code == ApiResultEnum.SUCCESS.code;
    }

    public ApiResult(T data) {
        this.data = data;
    }

    public ApiResult(Integer code, String msg, T data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public ApiResult(Integer code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public ApiResult(ApiResultEnum resultEnum) {
        this.msg = resultEnum.msg;
        this.code = resultEnum.code;
    }

    /**
     * 响应成功
     *
     * @return
     */
    public static <T> ApiResult<T> success() {
        return new ApiResult<>(ApiResultEnum.SUCCESS.code, ApiResultEnum.SUCCESS.msg);
    }

    /**
     * 响应成功
     *
     * @param data 结果数据
     * @return
     */
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(ApiResultEnum.SUCCESS.code, ApiResultEnum.SUCCESS.msg, data);
    }

    /**
     * 响应成功
     *
     * @param data 结果数据
     * @param msg  消息
     * @return
     */
    public static <T> ApiResult<T> success(T data, String msg) {
        return new ApiResult<>(ApiResultEnum.SUCCESS.code, msg, data);
    }

    /**
     * 响应失败
     *
     * @return
     */
    public static <T> ApiResult<T> failure() {
        return new ApiResult<>(ApiResultEnum.FAILURE.code, ApiResultEnum.FAILURE.msg);
    }

    /**
     * 响应失败
     *
     * @param msg 错误消息
     * @return
     */
    public static <T> ApiResult<T> failure(String msg) {
        return new ApiResult<>(ApiResultEnum.FAILURE.code, msg);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}
