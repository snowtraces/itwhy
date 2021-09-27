package com.snowtraces.itwhy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snowtraces.itwhy.dto.input.AnsGetInputDto;
import com.snowtraces.itwhy.dto.input.AnsSaveInputDto;
import com.snowtraces.itwhy.dto.output.AnsGetOutputDto;
import com.snowtraces.itwhy.dto.output.AnsSaveOutputDto;
import com.snowtraces.itwhy.entity.Ans;

/**
 * <p>
 * 答案 服务类
 * </p>
 *
 * @author snowtraces
 * @since 2021-09-26
 */
public interface AnsService extends IService<Ans> {

    /**
     * 保存回答
     *
     * @param inputDto
     * @return
     */
    AnsSaveOutputDto save(AnsSaveInputDto inputDto);

    /**
     * 查询回答
     *
     * @param inputDto
     * @return
     */
    AnsGetOutputDto get(AnsGetInputDto inputDto);

}
