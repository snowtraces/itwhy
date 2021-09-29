package com.snowtraces.itwhy.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snowtraces.itwhy.entity.Tag;
import com.snowtraces.itwhy.repository.TagMapper;
import com.snowtraces.itwhy.service.TagService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

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

    @Override
    public List<Long> fromName(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> tagIdList = new ArrayList<>();

        // 1. 数据库中已存在
        List<Tag> existTags = new LambdaQueryChainWrapper<>(baseMapper)
                .in(Tag::getTagName, tags)
                .list();
        existTags = existTags == null ? Collections.emptyList() : existTags;
        tagIdList.addAll(existTags.stream().map(Tag::getTagId).collect(toList()));

        // 2. 数据库中不存在
        List<String> existTagNames = existTags.stream().map(Tag::getTagName).collect(toList());
        List<String> newTagNames = tags.stream().filter(t -> !existTagNames.contains(t)).collect(toList());
        if (!newTagNames.isEmpty()) {
            List<Tag> newTags = newTagNames.stream().map(tn -> {
                Tag tag = new Tag();
                tag.setTagName(tn);
                return tag;
            }).collect(toList());

            super.saveOrUpdateBatch(newTags);

            tagIdList.addAll(newTags.stream().map(Tag::getTagId).collect(toList()));
        }

        return tagIdList;
    }
}
