package com.snowtraces.itwhy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snowtraces.itwhy.entity.Tag;

import java.util.List;

/**
 * <p>
 * 标签 服务类
 * </p>
 *
 * @author snowtraces
 * @since 2021-09-26
 */
public interface TagService extends IService<Tag> {

    /**
     * 标签转id
     *
     * @param tags
     * @return
     */
    List<Long> fromName(List<String> tags);
}
