package com.snowtraces.itwhy.dto.output;

import com.snowtraces.itwhy.util.SetData;
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
public class AnsGetOutputDto implements Serializable {
    /**
     * 答案ID
     */
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
     * 投票数值
     */
    private Integer voteCount;

    /**
     * 是否采纳
     */
    private Integer accepted;

}
