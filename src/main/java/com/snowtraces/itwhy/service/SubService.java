package com.snowtraces.itwhy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snowtraces.itwhy.dto.input.SubGetInputDto;
import com.snowtraces.itwhy.dto.input.SubSaveInputDto;
import com.snowtraces.itwhy.dto.output.SubGetOutputDto;
import com.snowtraces.itwhy.dto.output.SubSaveOutputDto;
import com.snowtraces.itwhy.entity.Sub;

/**
 * <p>
 * 问题 服务类
 * </p>
 *
 * @author snowtraces
 * @since 2021-09-26
 */
public interface SubService extends IService<Sub> {

    /**
     * 保存主题
     *
     * @param inputDto
     * @return
     */
    SubSaveOutputDto save(SubSaveInputDto inputDto);

    /**
     * 获取详情
     * @param inputDto
     * @return
     */
    SubGetOutputDto get(SubGetInputDto inputDto);
}
