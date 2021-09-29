package com.snowtraces.itwhy.controller;


import com.snowtraces.itwhy.dto.common.ApiResult;
import com.snowtraces.itwhy.dto.input.SubIdInputDto;
import com.snowtraces.itwhy.dto.input.SubListInputDto;
import com.snowtraces.itwhy.dto.input.SubSaveInputDto;
import com.snowtraces.itwhy.dto.output.SubGetOutputDto;
import com.snowtraces.itwhy.dto.output.SubListOutputDto;
import com.snowtraces.itwhy.dto.output.SubSaveOutputDto;
import com.snowtraces.itwhy.service.SubService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 问题 前端控制器
 * </p>
 *
 * @author snowtraces
 * @since 2021-09-26
 */
@RestController
@RequestMapping("/api/v1/sub")
public class SubController {

    @Autowired
    private SubService subService;

    @PostMapping("")
    public ApiResult<SubSaveOutputDto> save(@Valid @RequestBody SubSaveInputDto inputDto) {
        SubSaveOutputDto outputDto = subService.save(inputDto);
        return ApiResult.success(outputDto);
    }

    @GetMapping("")
    public ApiResult<SubGetOutputDto> get(@Valid SubIdInputDto inputDto) {
        SubGetOutputDto outputDto = subService.get(inputDto);
        return ApiResult.success(outputDto);
    }

    @DeleteMapping("")
    public ApiResult delete(@Valid SubIdInputDto inputDto) {
        subService.delete(inputDto);
        return ApiResult.success();
    }

    @GetMapping("/list")
    public ApiResult<List<SubListOutputDto>> list(@Valid SubListInputDto inputDto) {
        List<SubListOutputDto> outputDtoList = subService.list(inputDto);
        return ApiResult.success(outputDtoList);
    }
}

