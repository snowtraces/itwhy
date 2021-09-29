package com.snowtraces.itwhy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("qa_sub")
public class Sub implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 问题ID
     */
    @TableId(type = IdType.ASSIGN_ID)
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
     * 添加时间
     */
    private LocalDateTime addAt;

    /**
     * 来源ID
     */
    private String srcId;

    /**
     * tags
     */
    private String tags;
}
