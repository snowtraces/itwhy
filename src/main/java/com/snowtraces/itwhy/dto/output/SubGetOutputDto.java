package com.snowtraces.itwhy.dto.output;

import com.snowtraces.itwhy.util.SetData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
public class SubGetOutputDto implements Serializable {

    /**
     * 问题ID
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
     * 来源ID
     */
    private String srcId;

    /**
     * 答案集合
     */
    List<AnsGetOutputDto> ansList;

    /**
     * tags
     */
    private String tags;

    /**
     * 源数据
     */
    private SubSrcOutputDto subSrc;
}
