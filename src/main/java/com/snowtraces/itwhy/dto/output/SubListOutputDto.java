package com.snowtraces.itwhy.dto.output;

import com.snowtraces.itwhy.util.SetData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class SubListOutputDto implements Serializable {

    /**
     * 问题ID
     */
    private Long subId;

    /**
     * 问题标题
     */
    private String subTitle;

    /**
     * 人员ID
     */
    private Long addBy;

    /**
     * 用户名称
     */
    @SetData("addBy")
    private String addByName;

    /**
     * 添加时间
     */
    private LocalDateTime addAt;

    /**
     * tags
     */
    private String tags;
}
