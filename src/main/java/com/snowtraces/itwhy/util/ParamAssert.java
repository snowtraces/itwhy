package com.snowtraces.itwhy.util;


import com.snowtraces.itwhy.dto.common.ApiResultEnum;
import com.snowtraces.itwhy.dto.common.ParamValidateException;

import java.util.List;

/**
 * @author snow
 */
public class ParamAssert {

    /**
     * 参数非空验证
     *
     * @param value 参数值
     * @param msg   错误消息
     */
    public static void notBlank(String value, String msg) {
        if (value == null || value.isEmpty()) {
            throw new ParamValidateException(msg);
        }
    }

    /**
     * 参数非空验证
     *
     * @param value 参数值
     * @param msg   错误消息
     */
    public static void notBlank(Object value, String msg, Object... params) {
        if (value == null) {
            throw new ParamValidateException(spliceParam(msg, params));
        }
    }

    /**
     * 参数非空验证
     *
     * @param value 参数值
     */
    public static void notBlank(List<?> value, String msg, Object... params) {
        if (value == null || value.isEmpty()) {
            throw new ParamValidateException(spliceParam(msg, params));
        }
    }

    /**
     * 单个列表
     *
     * @param value 参数值
     */
    public static void single(List<?> value, String msg, Object... params) {
        notBlank(value, "不能为空");
        if (value.size() != 1) {
            throw new ParamValidateException(spliceParam(msg, params));
        }
    }

    /**
     * 参数非空验证
     *
     * @param value         参数值
     * @param apiResultEnum 错误枚举
     */
    public static void notBlank(Object value, ApiResultEnum apiResultEnum) {
        if (value == null) {
            throw new ParamValidateException(apiResultEnum);
        } else {
            if (value instanceof String) {
                if (((String) value).isEmpty()) {
                    throw new ParamValidateException(apiResultEnum);
                }
            }
        }
    }

    /**
     * boolean值为真验证
     *
     * @param value 参数值
     * @param msg   错误消息
     */
    public static void isTrue(boolean value, String msg, Object... params) {
        if (!value) {
            throw new ParamValidateException(spliceParam(msg, params));
        }
    }

    /**
     * boolean值为真验证
     */
    public static void isTrue(boolean value, ApiResultEnum apiResultEnum) {
        if (!value) {
            throw new ParamValidateException(apiResultEnum);
        }
    }

    /**
     * json 字符串验证
     */
    public static void isJsonString(String jsonString, String msg) {
        if (jsonString == null || jsonString.isEmpty()) {
            return;
        }
        ParamAssert.isTrue(
                (jsonString.matches("^\\[.*\\]$") || jsonString.matches("^\\{.*\\}$")),
                msg == null ? "参数不是json字符串" : msg
        );
    }

    /**
     * 参数拼接
     *
     * @param msgPattern 模板
     * @param params     参数
     * @return 拼接后的结果
     */
    private static String spliceParam(String msgPattern, Object... params) {
        if (msgPattern == null || msgPattern.isEmpty()) {
            return msgPattern;
        }

        if (params == null || params.length == 0) {
            return msgPattern;
        }

        int offset = 0;
        StringBuilder sb = new StringBuilder();

        for (Object param : params) {
            int idx = msgPattern.indexOf("{}", offset);
            if (idx == -1) {
                sb.append(msgPattern, offset, msgPattern.length());
                return sb.toString();
            }

            sb.append(msgPattern, offset, idx).append(param == null ? "null" : param.toString());
            offset = idx + 2;
        }

        sb.append(msgPattern, offset, msgPattern.length());
        return sb.toString();
    }
}
