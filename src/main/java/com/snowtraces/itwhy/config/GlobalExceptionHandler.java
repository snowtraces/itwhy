package com.snowtraces.itwhy.config;

import com.snowtraces.itwhy.dto.common.ApiResult;
import com.snowtraces.itwhy.dto.common.ApiResultEnum;
import com.snowtraces.itwhy.dto.common.ParamValidateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult<?> handleMethodArgumentNotValidException(
            HttpServletRequest request,
            HttpServletResponse response,
            MethodArgumentNotValidException exception
    ) {
        List<ObjectError> allErrors = exception.getBindingResult().getAllErrors();
        String msg = allErrors.get(0).getDefaultMessage();
        return ApiResult.failure(msg);
    }

    @ExceptionHandler(BindException.class)
    public ApiResult<?> handleBindException(
            HttpServletRequest request,
            HttpServletResponse response,
            BindException exception
    ) {
        List<FieldError> allErrors = exception.getFieldErrors();

        String msg = allErrors.get(0).getDefaultMessage();
        return ApiResult.failure(msg);
    }

    @ExceptionHandler(ParamValidateException.class)
    public ApiResult<?> handleParamValidateException(
            HttpServletRequest request,
            HttpServletResponse response,
            ParamValidateException exception
    ) {
        return new ApiResult<>(
                exception.getCode(),
                exception.getMessage(),
                exception.getData());
    }

    @ExceptionHandler(Exception.class)
    public ApiResult handleException(
            HttpServletRequest request,
            HttpServletResponse response,
            Exception exception) {
        log.error("", exception);
        return ApiResult.failure(exception.getMessage());
    }
}
