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
 * 答案源数据
 * </p>
 *
 * @author snowtraces
 * @since 2021-09-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("qa_ans_src")
public class AnsSrc implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 来源ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String srcId;

    /**
     * 回答详情
     */
    private String ansDesc;

    /**
     * 是否翻译
     */
    private Integer isTrans;


}
