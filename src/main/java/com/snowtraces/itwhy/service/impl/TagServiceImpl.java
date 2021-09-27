package com.snowtraces.itwhy.service.impl;

import com.snowtraces.itwhy.entity.Tag;
import com.snowtraces.itwhy.repository.TagMapper;
import com.snowtraces.itwhy.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标签 服务实现类
 * </p>
 *
 * @author snowtraces
 * @since 2021-09-26
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

}
