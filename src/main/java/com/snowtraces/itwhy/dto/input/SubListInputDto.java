package com.snowtraces.itwhy.dto.input;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class SubListInputDto {
    /**
     * 查询关键字
     */
    private String query;
}
