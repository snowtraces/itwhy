package com.snowtraces.itwhy.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snowtraces.itwhy.dto.input.SubGetInputDto;
import com.snowtraces.itwhy.dto.input.SubSaveInputDto;
import com.snowtraces.itwhy.dto.output.SubGetOutputDto;
import com.snowtraces.itwhy.dto.output.SubSaveOutputDto;
import com.snowtraces.itwhy.entity.Sub;
import com.snowtraces.itwhy.entity.SubSrc;
import com.snowtraces.itwhy.entity.User;
import com.snowtraces.itwhy.repository.SubMapper;
import com.snowtraces.itwhy.service.SubService;
import com.snowtraces.itwhy.service.SubSrcService;
import com.snowtraces.itwhy.service.UserService;
import com.snowtraces.itwhy.util.Constants;
import com.snowtraces.itwhy.util.DataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Override
    public SubSaveOutputDto save(SubSaveInputDto inputDto) {
        String srcId = inputDto.getSrcId();
        boolean isLocal = srcId == null;

        if (!isLocal) {
            // 远程采集
            Sub exist = new LambdaQueryChainWrapper<>(baseMapper)
                    .eq(Sub::getSrcId, srcId)
                    .one();
            if (exist != null) {
                return new SubSaveOutputDto(exist.getSubId());
            }

            // 1. 写源数据
            SubSrc subSrc = new SubSrc();
            subSrc.setSrcId(srcId);
            subSrc.setIsTrans(Constants.NO);
            subSrc.setSubTitle(inputDto.getSubTitle());
            subSrc.setSubDesc(inputDto.getSubDesc());
            subSrcService.save(subSrc);

            // 2. 写用户信息
            User user = userService.addBySrc(inputDto.getSrcUserId(),
                    inputDto.getSrcUserName(),
                    inputDto.getSrcPlat());

            // 3. 写本地数据
            Sub sub = new Sub();
            sub.setSrcId(srcId);
            sub.setSubTitle(inputDto.getSubTitle());
            sub.setSubDesc(inputDto.getSubDesc());
            sub.setAddAt(LocalDateTime.now());
            sub.setAddBy(user.getUserId());
            super.save(sub);

            return new SubSaveOutputDto(sub.getSubId());
        } else {
            // TODO 本地数据
            return null;
        }
    }

    @Override
    public SubGetOutputDto get(SubGetInputDto inputDto) {
        Long subId = inputDto.getSubId();
        Sub sub = new LambdaQueryChainWrapper<>(baseMapper)
                .eq(Sub::getSubId, subId)
                .one();
        SubGetOutputDto outputDto = DataConverter.toBean(sub, SubGetOutputDto.class);
        userService.forName(outputDto);
        return outputDto;
        
    }
}
