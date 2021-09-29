package com.snowtraces.itwhy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snowtraces.itwhy.dto.output.TagSubOutputDto;
import com.snowtraces.itwhy.entity.TagSub;

import java.util.List;

/**
 * <p>
 * 标签 服务类
 * </p>
 *
 * @author snowtraces
 * @since 2021-09-29
 */
public interface TagSubService extends IService<TagSub> {

    /**
     * 保存问题标签关联关系
     *
     * @param subId
     * @param tags
     */
    void save(Long subId, List<String> tags);

    /**
     * 按问题ID查询
     *
     * @param subId
     * @return
     */
    List<TagSubOutputDto> listBySubId(Long subId);
}
