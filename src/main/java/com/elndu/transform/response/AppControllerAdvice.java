package com.elndu.transform.response;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


@Slf4j
@ControllerAdvice
public class AppControllerAdvice implements ResponseBodyAdvice<Object>
{

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass)
    {
        return true;
    }

    /**
     * 封装返回结果
     */
    @Override
    public Object beforeBodyWrite(Object returnValue, MethodParameter methodParameter, MediaType mediaType,
                                  Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse)
    {

        //如果已经是ResponseData，直接返回
        if (returnValue instanceof ResponseData)
        {
            return returnValue;
        }
        else if (returnValue instanceof String)
        {
            try
            {
                return JSONObject.toJSONString(SuccessResponseData.success(returnValue));
            }
            catch (Exception e)
            {
                log.error("返回结果转换json异常", e);
                return ErrorResponseData.error("返回结果转换json异常");
            }
        }
        return SuccessResponseData.success(returnValue);
    }
}
