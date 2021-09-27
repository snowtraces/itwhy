package com.snowtraces.itwhy.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snowtraces.itwhy.dto.input.AnsGetInputDto;
import com.snowtraces.itwhy.dto.input.AnsSaveInputDto;
import com.snowtraces.itwhy.dto.output.AnsGetOutputDto;
import com.snowtraces.itwhy.dto.output.AnsSaveOutputDto;
import com.snowtraces.itwhy.entity.Ans;
import com.snowtraces.itwhy.entity.AnsSrc;
import com.snowtraces.itwhy.entity.User;
import com.snowtraces.itwhy.repository.AnsMapper;
import com.snowtraces.itwhy.service.AnsService;
import com.snowtraces.itwhy.service.AnsSrcService;
import com.snowtraces.itwhy.service.UserService;
import com.snowtraces.itwhy.util.Constants;
import com.snowtraces.itwhy.util.DataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 答案 服务实现类
 * </p>
 *
 * @author snowtraces
 * @since 2021-09-26
 */
@Service
public class AnsServiceImpl extends ServiceImpl<AnsMapper, Ans> implements AnsService {

    @Autowired
    private AnsSrcService ansSrcService;

    @Autowired
    private UserService userService;

    @Override
    public AnsSaveOutputDto save(AnsSaveInputDto inputDto) {
        String srcId = inputDto.getSrcId();
        boolean isLocal = srcId == null;

        if (!isLocal) {
            // 远程采集
            Ans exist = new LambdaQueryChainWrapper<>(baseMapper)
                    .eq(Ans::getSrcId, srcId)
                    .one();
            if (exist != null) {
                return new AnsSaveOutputDto(exist.getAnsId());
            }

            // 1. 写源数据
            AnsSrc ansSrc = new AnsSrc();
            ansSrc.setSrcId(srcId);
            ansSrc.setIsTrans(Constants.NO);
            ansSrc.setAnsDesc(inputDto.getAnsDesc());
            ansSrcService.save(ansSrc);

            // 2. 写用户信息
            User user = userService.addBySrc(inputDto.getSrcUserId(),
                    inputDto.getSrcUserName(),
                    inputDto.getSrcPlat());

            // 3. 写本地数据
            Ans ans = new Ans();
            ans.setSrcId(srcId);
            ans.setAnsDesc(inputDto.getAnsDesc());
            ans.setAddAt(LocalDateTime.now());
            ans.setAddBy(user.getUserId());
            ans.setSubId(inputDto.getSubId());
            super.save(ans);

            return new AnsSaveOutputDto(ans.getAnsId());
        } else {
            // TODO 本地数据
            return null;
        }
    }

    @Override
    public AnsGetOutputDto get(AnsGetInputDto inputDto) {
        Long ansId = inputDto.getAnsId();
        Ans ans = new LambdaQueryChainWrapper<>(baseMapper)
                .eq(Ans::getAnsId, ansId)
                .one();
        AnsGetOutputDto outputDto = DataConverter.toBean(ans, AnsGetOutputDto.class);
        userService.forName(outputDto);
        return outputDto;
    }
}
