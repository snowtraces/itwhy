package com.snowtraces.itwhy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("qa_tag_sub")
public class TagSub implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 标签ID
     */
      private Long tagId;

    /**
     * 问题ID
     */
    private Long subId;


}
