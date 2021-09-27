package com.snowtraces.itwhy.controller;


import com.snowtraces.itwhy.dto.common.ApiResult;
import com.snowtraces.itwhy.dto.input.AnsGetInputDto;
import com.snowtraces.itwhy.dto.input.AnsSaveInputDto;
import com.snowtraces.itwhy.dto.output.AnsGetOutputDto;
import com.snowtraces.itwhy.dto.output.AnsSaveOutputDto;
import com.snowtraces.itwhy.service.AnsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 答案 前端控制器
 * </p>
 *
 * @author snowtraces
 * @since 2021-09-26
 */
@RestController
@RequestMapping("/api/v1/ans")
public class AnsController {

    @Autowired
    private AnsService ansService;

    @PostMapping("")
    public ApiResult<AnsSaveOutputDto> save(@Valid @RequestBody AnsSaveInputDto inputDto) {
        AnsSaveOutputDto outputDto = ansService.save(inputDto);
        return ApiResult.success(outputDto);
    }
    
    @GetMapping("")
    public ApiResult<AnsGetOutputDto> get(@Valid AnsGetInputDto inputDto) {
        return ApiResult.success(ansService.get(inputDto));
    }

}

