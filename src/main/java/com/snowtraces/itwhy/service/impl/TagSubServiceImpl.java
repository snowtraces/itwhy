package com.snowtraces.itwhy.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snowtraces.itwhy.dto.output.TagSubOutputDto;
import com.snowtraces.itwhy.entity.TagSub;
import com.snowtraces.itwhy.repository.TagSubMapper;
import com.snowtraces.itwhy.service.TagService;
import com.snowtraces.itwhy.service.TagSubService;
import com.snowtraces.itwhy.util.DataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 * 标签 服务实现类
 * </p>
 *
 * @author snowtraces
 * @since 2021-09-29
 */
@Service
public class TagSubServiceImpl extends ServiceImpl<TagSubMapper, TagSub> implements TagSubService {

    @Autowired
    private TagService tagService;

    @Override
    public void save(Long subId, List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return;
        }

        List<TagSubOutputDto> exists = listBySubId(subId);
        if (exists != null && !exists.isEmpty()) {
            return;
        }

        // 1. 标签 name -> id
        List<Long> tagIds = tagService.fromName(tags);

        // 2. 保存
        List<TagSub> tagSubList = tagIds.stream().map(tagId -> {
            TagSub tagSub = new TagSub();
            tagSub.setSubId(subId);
            tagSub.setTagId(tagId);
            return tagSub;
        }).collect(toList());

        super.saveBatch(tagSubList);
    }

    @Override
    public List<TagSubOutputDto> listBySubId(Long subId) {
        List<TagSub> list = new LambdaQueryChainWrapper<>(baseMapper)
                .eq(TagSub::getSubId, subId)
                .list();

        if (list == null) {
            return Collections.emptyList();
        } else {
            return list.stream().map(ts -> DataConverter.toBean(ts, TagSubOutputDto.class)).collect(toList());
        }
    }
}
