package com.elndu.transform.exception;

import com.elndu.transform.enums.CommonExceptionEnum;
import com.elndu.transform.response.ErrorResponseData;
import com.elndu.transform.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局的的异常拦截器
 */
@Slf4j
@ControllerAdvice
public class DefaultControllerAdvice
{
    /**
     * 拦截common异常
     */
    @ExceptionHandler(CommonException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseData serviceException(CommonException e)
    {
        log.error("服务端业务异常:", e);
        return new ErrorResponseData(e.getCode(), e.getErrorMessage());
    }


    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseData notFount(Exception e, HttpServletResponse reponse)
    {
        if (HttpStatus.NOT_FOUND.value() == reponse.getStatus())
        {
            log.error(CommonExceptionEnum.NO_FOUNT.getMessage(), e);
            return new ErrorResponseData(CommonExceptionEnum.NO_FOUNT.getCode(), CommonExceptionEnum.NO_FOUNT
                    .getMessage());
        }
        reponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        if (e instanceof BindException)
        {
            log.error("参数校验异常", e);
            return new ErrorResponseData(CommonExceptionEnum.SYSTEM_ERROR.getCode(), e.getMessage());
        }
        if (e instanceof MethodArgumentNotValidException)
        {
            log.error("Bean validator exception", e);
            MethodArgumentNotValidException validException = (MethodArgumentNotValidException) e;
            List<ObjectError> errors = validException.getBindingResult().getAllErrors();
            List<String> errorMessages = errors.stream()
                    .map(error -> error.getDefaultMessage() + "[" + (error instanceof FieldError ?
                            ((FieldError) error).getField() :
                            error.getObjectName()) + "]").collect(Collectors.toList());
            return new ErrorResponseData(CommonExceptionEnum.SYSTEM_ERROR.getCode(), String.join("; ", errorMessages));
        }

        //子异常 修复异常转换的问题
        if (e.getCause() instanceof CommonException)
        {
            return serviceException((CommonException) e.getCause());
        }

        e.printStackTrace();
        return new ErrorResponseData(CommonExceptionEnum.SYSTEM_ERROR.getCode(), CommonExceptionEnum.SYSTEM_ERROR
                .getMessage());
    }
}
