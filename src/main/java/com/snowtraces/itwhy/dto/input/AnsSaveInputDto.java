package com.snowtraces.itwhy.dto.input;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
public class AnsSaveInputDto extends BaseSrcDto implements Serializable {

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
     * 投票数值
     */
    private Integer voteCount;

    /**
     * 是否采纳
     */
    private Integer accepted;

}
