package com.snowtraces.itwhy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 答案
 * </p>
 *
 * @author snowtraces
 * @since 2021-09-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("qa_ans")
public class Ans implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 答案ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long ansId;

    /**
     * 问题ID
     */
    @NotNull
    private Long subId;

    /**
     * 回答详情
     */
    private String ansDesc;

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
