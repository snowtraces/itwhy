package com.snowtraces.itwhy.dto.common;

/**
 * 参数验证，自定义异常
 *
 * @author CHENG
 */
public class ParamValidateException extends RuntimeException {
    private Integer code;
    private String message;
    private Object data;

    public ParamValidateException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public ParamValidateException(ApiResultEnum apiResultEnum) {
        super(apiResultEnum.msg);
        this.code = apiResultEnum.code;
    }

    public ParamValidateException(ApiResultEnum apiResultEnum, Object data) {
        super(apiResultEnum.msg);
        this.code = apiResultEnum.code;
        this.data = data;
    }

    public ParamValidateException(String message) {
        super(message);
        this.code = ApiResultEnum.FAILURE.code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}