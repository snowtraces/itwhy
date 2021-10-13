package com.snowtraces.itwhy.dto.input;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 问题
 * </p>
 *
 * @author snowtraces
 * @since 2021-09-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SubEditInputDto implements Serializable {

    /**
     * 话题ID
     */
    private Long subId;

    /**
     * 问题标题
     */
    private String subTitle;

    /**
     * 问题详情
     */
    private String subDesc;

    /**
     * tags
     */
    private String tags;

}
