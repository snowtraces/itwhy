package com.snowtraces.itwhy.dto.output;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 问题源数据
 * </p>
 *
 * @author snowtraces
 * @since 2021-09-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SubSrcOutputDto implements Serializable {

    /**
     * 来源ID
     */
    private String srcId;

    /**
     * 问题标题
     */
    private String subTitle;

    /**
     * 问题详情
     */
    private String subDesc;

    /**
     * 来源平台
     */
    private String srcPlat;

    /**
     * 是否翻译
     */
    private Integer isTrans;

}
