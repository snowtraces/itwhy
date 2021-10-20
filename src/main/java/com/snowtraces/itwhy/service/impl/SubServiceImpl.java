package com.snowtraces.itwhy.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snowtraces.itwhy.dto.input.*;
import com.snowtraces.itwhy.dto.output.*;
import com.snowtraces.itwhy.entity.Sub;
import com.snowtraces.itwhy.entity.SubSrc;
import com.snowtraces.itwhy.entity.User;
import com.snowtraces.itwhy.repository.SubMapper;
import com.snowtraces.itwhy.service.*;
import com.snowtraces.itwhy.util.Constants;
import com.snowtraces.itwhy.util.DataConverter;
import com.snowtraces.itwhy.util.ParamAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.snowtraces.itwhy.util.TransUtils.translate;

/**
 * <p>
 * 问题 服务实现类
 * </p>
 *
 * @author snowtraces
 * @since 2021-09-26
 */
@Service
public class SubServiceImpl extends ServiceImpl<SubMapper, Sub> implements SubService {
    @Autowired
    private SubSrcService subSrcService;

    @Autowired
    private UserService userService;

    @Autowired
    private AnsService ansService;

    @Autowired
    private TagSubService tagSubService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SubSaveOutputDto save(SubSaveInputDto inputDto) {
        String srcId = inputDto.getSrcId();
        String tags = inputDto.getTags();
        List<String> tagsList = tags == null ? Collections.emptyList() : Arrays.asList(tags.split(","));
        boolean isLocal = srcId == null;

        if (!isLocal) {
            // 远程采集
            Sub exist = new LambdaQueryChainWrapper<>(baseMapper)
                    .eq(Sub::getSrcId, srcId)
                    .one();

            // 1. 写源数据
            SubSrc subSrc = new SubSrc();
            subSrc.setSrcId(srcId);
            subSrc.setIsTrans(Constants.YES);
            subSrc.setSubTitle(inputDto.getSubTitle());
            subSrc.setSubDesc(inputDto.getSubDesc());
            subSrcService.saveOrUpdate(subSrc);

            // 2. 写用户信息
            User user = userService.addBySrc(inputDto.getSrcUserId(),
                    inputDto.getSrcUserName(),
                    inputDto.getSrcPlat());

            // 3. 写本地数据
            Sub sub = new Sub();
            sub.setSrcId(srcId);
            if (exist != null) {
                sub.setSubId(exist.getSubId());
            }
            sub.setSubTitle(translate(inputDto.getSubTitle()));
            sub.setSubDesc(translate(inputDto.getSubDesc()));
            sub.setAddAt(LocalDateTime.now());
            sub.setAddBy(user.getUserId());
            sub.setTags(tags);
            super.saveOrUpdate(sub);

            // 4. 保存标签
            tagSubService.save(sub.getSubId(), tagsList);

            return new SubSaveOutputDto(sub.getSubId());
        } else {
            // TODO 本地数据
            return null;
        }
    }

    @Override
    public SubGetOutputDto get(SubGetInputDto inputDto) {
        Long subId = inputDto.getSubId();

        // 1. 查询问题
        Sub sub = new LambdaQueryChainWrapper<>(baseMapper)
                .eq(Sub::getSubId, subId)
                .one();
        ParamAssert.notBlank(sub, "没有找到数据：{}", inputDto.getSubId());
        SubGetOutputDto outputDto = DataConverter.toBean(sub, SubGetOutputDto.class);
        userService.forName(outputDto);

        // 2. 查询回答
        List<AnsGetOutputDto> ansList = ansService.listBySubId(subId);
        outputDto.setAnsList(ansList);

        // 3. 查询源内容
        if (inputDto.isQuerySrc()) {
            SubSrc subSrc = subSrcService.getById(sub.getSrcId());
            outputDto.setSubSrc(DataConverter.toBean(subSrc, SubSrcOutputDto.class));
        }

        return outputDto;

    }

    @Override
    public void delete(SubIdInputDto inputDto) {
        super.removeById(inputDto.getSubId());
    }

    @Override
    public List<SubListOutputDto> list(SubListInputDto inputDto) {
        String query = inputDto.getQuery();
        String tag = inputDto.getTag();
        List<Sub> subList = new LambdaQueryChainWrapper<>(baseMapper)
                .select(Sub::getSubId,
                        Sub::getSubTitle,
                        Sub::getAddBy,
                        Sub::getAddAt,
                        Sub::getTags)
                .like(query != null && !query.isEmpty(), Sub::getSubTitle, query)
                .like(tag != null && !tag.isEmpty(), Sub::getTags, tag)
                .orderByDesc(Sub::getAddAt)
                .list();
        if (subList == null || subList.isEmpty()) {
            return Collections.emptyList();
        } else {
            return subList.stream().map(sub -> {
                SubListOutputDto outputDto = DataConverter.toBean(sub, SubListOutputDto.class);
                userService.forName(outputDto);
                return outputDto;
            }).collect(Collectors.toList());
        }

    }

    @Override
    public SubSaveOutputDto edit(SubEditInputDto inputDto) {
        new LambdaUpdateChainWrapper<>(baseMapper)
                .eq(Sub::getSubId, inputDto.getSubId())
                .set(Sub::getSubDesc, inputDto.getSubDesc())
                .set(Sub::getSubTitle, inputDto.getSubTitle())
                .update();
        return new SubSaveOutputDto(inputDto.getSubId());
    }


    public static void main(String[] args) {
        // \u000d System.out.println("Hello World!");
    }

}
