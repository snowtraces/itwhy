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
 * 评论
 * </p>
 *
 * @author snowtraces
 * @since 2021-09-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("qa_com")
public class Com implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long comId;

    /**
     * 关联数据类型
     */
    private Integer dataOf;

    /**
     * 数据ID
     */
    private Long dataId;

    /**
     * 评论详情
     */
    private String comDesc;

    /**
     * 用户ID
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


}
