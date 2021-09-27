package com.snowtraces.itwhy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("qa_sub_src")
public class SubSrc implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 来源ID
     */
    @TableId(type = IdType.ASSIGN_ID)
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
