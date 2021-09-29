package com.snowtraces.itwhy.dto.output;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 标签
 * </p>
 *
 * @author snowtraces
 * @since 2021-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TagSubOutputDto implements Serializable {


    /**
     * 标签ID
     */
    private Long tagId;

    /**
     * 问题ID
     */
    private Long subId;


}
