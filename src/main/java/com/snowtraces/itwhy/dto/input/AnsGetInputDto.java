package com.snowtraces.itwhy.dto.input;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class AnsGetInputDto {
    /**
     * 答案ID
     */
    @NotNull
    private Long ansId;
}
