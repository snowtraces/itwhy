package com.snowtraces.itwhy.dto.input;

import lombok.Data;

@Data
public class BaseSrcDto {
    /**
     * 来源ID
     */
    private String srcId;

    /**
     * 用户ID
     */
    private String srcUserId;

    /**
     * 用户名
     */
    private String srcUserName;

    /**
     * 来源平台
     */
    private String srcPlat;
}
